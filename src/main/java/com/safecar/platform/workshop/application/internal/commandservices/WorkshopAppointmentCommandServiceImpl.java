package com.safecar.platform.workshop.application.internal.commandservices;

import org.springframework.stereotype.Service;

import com.safecar.platform.workshop.application.internal.outboundservices.acl.ExternalDeviceService;
import com.safecar.platform.workshop.application.internal.outboundservices.acl.ExternalIamService;
import com.safecar.platform.workshop.domain.model.aggregates.WorkshopAppointment;
import com.safecar.platform.workshop.domain.model.aggregates.WorkshopOrder;
import com.safecar.platform.workshop.domain.model.commands.*;
import com.safecar.platform.workshop.domain.model.valueobjects.AppointmentStatus;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkOrderStatus;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkOrderCode;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;
import com.safecar.platform.workshop.domain.model.valueobjects.VehicleId;
import com.safecar.platform.workshop.domain.model.valueobjects.DriverId;
import com.safecar.platform.workshop.domain.services.WorkshopAppointmentCommandService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.WorkshopAppointmentRepository;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.WorkshopOrderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Workshop Appointment Command Service Implementation with ACL Integration
 * 
 * Enhanced with comprehensive validation through external services:
 * - IAM context for driver validation
 * - Devices context for vehicle validation
 * 
 * @see WorkshopAppointmentCommandService WorkshopAppointmentCommandService for
 *      method details.
 */
@Service
public class WorkshopAppointmentCommandServiceImpl implements WorkshopAppointmentCommandService {

    private static final Logger logger = LoggerFactory.getLogger(WorkshopAppointmentCommandServiceImpl.class);

    /**
     * Repository for WorkshopAppointment to handle persistence operations.
     */
    private final WorkshopAppointmentRepository repository;
    private final WorkshopOrderRepository orderRepository;
    
    /**
     * Service for IAM bounded context interactions.
     */
    private final ExternalIamService externalIamService;
    
    /**
     * Service for Devices bounded context interactions.
     */
    private final ExternalDeviceService externalDeviceService;

    /**
     * Constructor for WorkshopAppointmentCommandServiceImpl.
     * 
     * @param repository Repository for WorkshopAppointment.
     * @param orderRepository Repository for WorkshopOrder.
     * @param externalIamService Service for IAM bounded context interactions.
     * @param externalDeviceService Service for Devices bounded context interactions.
     */
    public WorkshopAppointmentCommandServiceImpl(WorkshopAppointmentRepository repository, 
                                                WorkshopOrderRepository orderRepository,
                                                ExternalIamService externalIamService,
                                                ExternalDeviceService externalDeviceService) {
        this.repository = repository;
        this.orderRepository = orderRepository;
        this.externalIamService = externalIamService;
        this.externalDeviceService = externalDeviceService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(CreateAppointmentCommand command) {
        logger.info("Processing appointment creation for workshop: {} and vehicle: {}", 
                   command.workshopId(), command.vehicleId());

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

            // Check for appointment slot conflicts
            var existingAppointments = repository.findByWorkOrderWorkshop(command.workshopId());

            var hasOverlap = existingAppointments.stream()
                    .filter(appointment -> !AppointmentStatus.CANCELLED.equals(appointment.getStatus()))
                    .anyMatch(appointment -> appointment.getScheduledAt().overlapsWith(command.slot()));

            if (hasOverlap)
                throw new IllegalStateException("Appointment slot overlaps with existing appointment");

            // Find or create a work order for this service context
            var workOrder = findOrCreateWorkOrder(command.workshopId(), command.vehicleId(), command.driverId());
            
            var appointment = new WorkshopAppointment(command, workOrder);
            repository.save(appointment);
            
            logger.info("Successfully created appointment for driver: {} and vehicle: {}", driverId, vehicleId);

            // TODO: Add notification service when Notification BC is implemented
            
        } catch (Exception e) {
            logger.error("Failed to create appointment for driver: {} and vehicle: {} - Error: {}", 
                        driverId, vehicleId, e.getMessage());
            throw e; // Re-throw to maintain transactional behavior
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(LinkAppointmentToWorkOrderCommand command) {
        var appointment = repository.findById(command.appointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        var code = command.workOrderCode();

        // quick validation that the code was issued for the same workshop
        if (!appointment.getWorkshop().workshopId().equals(code.issuedByWorkshopId())) {
            throw new IllegalStateException("Work order code was not issued for the appointment workshop");
        }

        var maybeOrder = orderRepository.findByCodeValueAndWorkshop_WorkshopId(code.value(), code.issuedByWorkshopId());
        var order = maybeOrder.orElseThrow(() -> new IllegalArgumentException("Work order not found for provided code"));

        appointment.updateWorkOrder(order, code);
        repository.save(appointment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(RescheduleAppointmentCommand command) {
        var appointment = repository.findById(command.appointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        
        var existingAppointments = repository.findByWorkOrderWorkshop(appointment.getWorkshop());

        var hasOverlap = existingAppointments.stream()
                .filter(a -> !a.getId().equals(appointment.getId()))
                .filter(a -> !AppointmentStatus.CANCELLED.equals(a.getStatus()))
                .anyMatch(a -> a.getScheduledAt().overlapsWith(command.slot()));

        if (hasOverlap)
            throw new IllegalStateException("New appointment slot overlaps with existing appointment");

        appointment.reschedule(command.slot());
        repository.save(appointment);

        // TODO: Add notification service when Notification BC is implemented
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(CancelAppointmentCommand command) {
        var appointment = repository.findById(command.appointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        appointment.cancel();
        repository.save(appointment);

        // TODO: Add notification service when Notification BC is implemented
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(AddAppointmentNoteCommand command) {
        var appointment = repository.findById(command.appointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        appointment.addNote(command.content(), command.authorId());
        repository.save(appointment);
    }

    /**
     * Find or create a work order for the given service context.
     * This eliminates redundancy by ensuring appointments are always linked to a work order
     * that contains the workshop, vehicle, and driver IDs.
     */
    private WorkshopOrder findOrCreateWorkOrder(WorkshopId workshopId, VehicleId vehicleId, DriverId driverId) {
        // Try to find an existing open work order for this workshop
        var existingOrders = orderRepository.findByWorkshopAndStatus(workshopId, WorkOrderStatus.OPEN);
        
        // Look for an order that matches the vehicle and driver context
        for (var order : existingOrders) {
            if (order.getVehicle().equals(vehicleId) && order.getDriver().equals(driverId)) {
                return order;
            }
        }
        
        // Create a new work order if none exists for this context
        var codeValue = "WO-" + System.currentTimeMillis(); // Simple code generation
        var workOrderCode = new WorkOrderCode(codeValue, workshopId.workshopId());
        var openCommand = new OpenWorkOrderCommand(
                workshopId, vehicleId, driverId, workOrderCode, null);
        
        var newWorkOrder = new WorkshopOrder(openCommand);
        return orderRepository.save(newWorkOrder);
    }
}