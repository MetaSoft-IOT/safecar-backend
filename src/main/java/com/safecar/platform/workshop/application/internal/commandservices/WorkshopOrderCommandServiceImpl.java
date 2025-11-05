package com.safecar.platform.workshop.application.internal.commandservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.safecar.platform.workshop.application.internal.outboundservices.acl.ExternalDeviceService;
import com.safecar.platform.workshop.application.internal.outboundservices.acl.ExternalIamService;
import com.safecar.platform.workshop.domain.model.aggregates.WorkshopOrder;
import com.safecar.platform.workshop.domain.model.commands.*;
import com.safecar.platform.workshop.domain.services.WorkshopOrderCommandService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.WorkshopOrderRepository;

/**
 * Workshop Order Command Service Implementation with ACL Integration
 * 
 * Enhanced with comprehensive validation through external services:
 * - IAM context for driver validation  
 * - Devices context for vehicle validation
 * 
 * @see WorkshopOrderCommandService WorkshopOrderCommandService for method
 *      details.
 */
@Service
public class WorkshopOrderCommandServiceImpl implements WorkshopOrderCommandService {

    private static final Logger logger = LoggerFactory.getLogger(WorkshopOrderCommandServiceImpl.class);

    /**
     * Repository for WorkshopOrder to handle persistence operations.
     */
    private final WorkshopOrderRepository repository;
    
    /**
     * Service for IAM bounded context interactions.
     */
    private final ExternalIamService externalIamService;
    
    /**
     * Service for Devices bounded context interactions.
     */
    private final ExternalDeviceService externalDeviceService;

    /**
     * Constructor for WorkshopOrderCommandServiceImpl.
     * 
     * @param repository Repository for WorkshopOrder to handle persistence operations.
     * @param externalIamService Service for IAM bounded context interactions.
     * @param externalDeviceService Service for Devices bounded context interactions.
     */
    public WorkshopOrderCommandServiceImpl(WorkshopOrderRepository repository,
                                          ExternalIamService externalIamService,
                                          ExternalDeviceService externalDeviceService) {
        this.repository = repository;
        this.externalIamService = externalIamService;
        this.externalDeviceService = externalDeviceService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(OpenWorkOrderCommand command) {
        logger.info("Processing work order opening for workshop: {}, vehicle: {}, driver: {}", 
                   command.workshopId(), command.vehicleId(), command.driverId());

        var driverId = command.driverId().driverId();
        var vehicleId = command.vehicleId();

        try {
            // Validate driver exists in IAM context
            if (!externalIamService.validateDriverExists(driverId)) {
                throw new IllegalArgumentException("Driver with ID " + driverId + " does not exist");
            }
            logger.debug("Driver validation successful for driverId: {}", driverId);

            // Validate vehicle exists in Devices context
            if (!externalDeviceService.validateVehicleExists(vehicleId)) {
                throw new IllegalArgumentException("Vehicle with ID " + vehicleId + " does not exist");
            }
            logger.debug("Vehicle validation successful for vehicleId: {}", vehicleId);

            // Validate driver owns the vehicle
            if (!externalDeviceService.validateDriverOwnsVehicle(vehicleId, driverId)) {
                throw new IllegalArgumentException("Driver " + driverId + " does not own vehicle " + vehicleId);
            }
            logger.debug("Driver-vehicle relationship validation successful");

            // Check for work order code uniqueness
            var exists = repository.existsByCodeValueAndWorkshop_WorkshopId(command.code().value(),
                    command.workshopId().workshopId());
                    
            if (exists)
                throw new IllegalStateException("Work order code already exists for this workshop");

            var wo = new WorkshopOrder(command);
            repository.save(wo);
            
            logger.info("Successfully opened work order {} for driver: {} and vehicle: {}", 
                       command.code().value(), driverId, vehicleId);

        } catch (Exception e) {
            logger.error("Failed to open work order for driver: {} and vehicle: {} - Error: {}", 
                        driverId, vehicleId, e.getMessage());
            throw e; // Re-throw to maintain transactional behavior
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(CloseWorkOrderCommand command) {
        var wo = repository.findById(command.workOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Work order not found"));
        wo.close();
        repository.save(wo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(AddAppointmentToWorkOrderCommand command) {
        var wo = repository.findById(command.workOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Work order not found"));
        wo.addAppointment(command.slot());
        repository.save(wo);
    }
}
