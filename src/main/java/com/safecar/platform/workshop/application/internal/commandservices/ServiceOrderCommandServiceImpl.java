package com.safecar.platform.workshop.application.internal.commandservices;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.safecar.platform.workshop.application.internal.outboundservices.acl.ExternalDeviceService;
import com.safecar.platform.workshop.application.internal.outboundservices.acl.ExternalIamService;
import com.safecar.platform.workshop.domain.model.aggregates.ServiceOrder;
import com.safecar.platform.workshop.domain.model.commands.*;
import com.safecar.platform.workshop.domain.services.ServiceOrderCommandService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.ServiceOrderRepository;

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
public class ServiceOrderCommandServiceImpl implements ServiceOrderCommandService {

    private final ServiceOrderRepository repository;
    private final ExternalIamService externalIamService;
    private final ExternalDeviceService externalDeviceService;

    /**
     * Constructor for ServiceOrderCommandServiceImpl.
     * 
     * @param repository            Repository for ServiceOrder to handle
     *                              persistence operations.
     * @param externalIamService    Service for IAM bounded context interactions.
     * @param externalDeviceService Service for Devices bounded context
     *                              interactions.
     */
    public ServiceOrderCommandServiceImpl(ServiceOrderRepository repository,
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
    public Optional<ServiceOrder> handle(OpenServiceOrderCommand command) {
        var driverId = command.driverId().driverId();
        var vehicleId = command.vehicleId();

        if (!externalIamService.validateDriverExists(driverId))
            throw new IllegalArgumentException("Driver with ID " + driverId + " does not exist");

        if (!externalDeviceService.validateVehicleExists(vehicleId))
            throw new IllegalArgumentException("Vehicle with ID " + vehicleId + " does not exist");

        if (!externalDeviceService.validateDriverOwnsVehicle(vehicleId, driverId))
            throw new IllegalArgumentException("Driver " + driverId + " does not own vehicle " + vehicleId);

        var exists = repository.existsByCodeValueAndWorkshop_WorkshopId(command.code().value(),
                command.workshopId().workshopId());

        if (exists)
            throw new IllegalStateException("Service order code already exists for this workshop");

        var serviceOrder = new ServiceOrder(command);
        repository.save(serviceOrder);

        return Optional.of(serviceOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ServiceOrder> handle(CloseServiceOrderCommand command) {
        var serviceOrder = repository.findById(command.serviceOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Service order not found"));

        boolean wasClosed = serviceOrder.close();

        if (!wasClosed) {
            return Optional.empty();
        }

        repository.save(serviceOrder);
        return Optional.of(serviceOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ServiceOrder> handle(AddAppointmentToServiceOrderCommand command) {
        var so = repository.findById(command.serviceOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Service order not found"));
        so.addAppointment(command.slot());
        repository.save(so);
        return Optional.of(so);
    }
}
