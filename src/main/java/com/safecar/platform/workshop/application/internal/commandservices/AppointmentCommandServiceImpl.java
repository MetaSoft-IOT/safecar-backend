package com.safecar.platform.workshop.application.internal.commandservices;

import org.springframework.stereotype.Service;

import com.safecar.platform.workshop.application.internal.outboundservices.acl.ExternalDeviceService;
import com.safecar.platform.workshop.application.internal.outboundservices.acl.ExternalIamService;
import com.safecar.platform.workshop.domain.model.aggregates.Appointment;
import com.safecar.platform.workshop.domain.model.aggregates.ServiceOrder;
import com.safecar.platform.workshop.domain.model.commands.*;
import com.safecar.platform.workshop.domain.model.valueobjects.AppointmentStatus;
import com.safecar.platform.workshop.domain.model.valueobjects.ServiceOrderStatus;
import com.safecar.platform.workshop.domain.model.valueobjects.ServiceOrderCode;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;
import com.safecar.platform.workshop.domain.model.valueobjects.VehicleId;
import com.safecar.platform.workshop.domain.model.valueobjects.DriverId;
import com.safecar.platform.workshop.domain.services.AppointmentCommandService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.AppointmentRepository;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.ServiceOrderRepository;

import java.util.Optional;

/**
 * Appointment Command Service Implementation (sin logging, sin try/catch)
 */
@Service
public class AppointmentCommandServiceImpl implements AppointmentCommandService {

    private final AppointmentRepository repository;
    private final ServiceOrderRepository orderRepository;
    private final ExternalIamService externalIamService;
    private final ExternalDeviceService externalDeviceService;

    public AppointmentCommandServiceImpl(AppointmentRepository repository,
            ServiceOrderRepository orderRepository,
            ExternalIamService externalIamService,
            ExternalDeviceService externalDeviceService) {
        this.repository = repository;
        this.orderRepository = orderRepository;
        this.externalIamService = externalIamService;
        this.externalDeviceService = externalDeviceService;
    }

    // {@inheritDoc}
    @Override
    public Optional<Appointment> handle(CreateAppointmentCommand command) {
        var driverId = command.driverId().driverId();
        var vehicleId = command.vehicleId();

        if (!externalIamService.validateDriverExists(driverId))
            throw new IllegalArgumentException("Driver with ID " + driverId + " does not exist");

        if (!externalDeviceService.validateVehicleExists(vehicleId))
            throw new IllegalArgumentException("Vehicle with ID " + vehicleId + " does not exist");

        if (!externalDeviceService.validateDriverOwnsVehicle(vehicleId, driverId))
            throw new IllegalArgumentException("Driver " + driverId + " does not own vehicle " + vehicleId);

        var existingAppointments = repository.findByServiceOrderWorkshop(command.workshopId());

        var hasOverlap = existingAppointments.stream()
                .filter(a -> !AppointmentStatus.CANCELLED.equals(a.getStatus()))
                .anyMatch(a -> a.getScheduledAt().overlapsWith(command.slot()));

        if (hasOverlap)
            throw new IllegalStateException("Appointment slot overlaps with existing appointment");

        var serviceOrder = findOrCreateServiceOrder(command.workshopId(), command.vehicleId(), command.driverId());

        var saved = repository.save(new Appointment(command, serviceOrder));

        return Optional.of(saved);
    }

    // {@inheritDoc}
    @Override
    public Optional<Appointment> handle(LinkAppointmentToServiceOrderCommand command) {
        var appointment = repository.findById(command.appointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        var code = command.serviceOrderCode();

        if (!appointment.getWorkshopId().equals(code.issuedByWorkshopId()))
            throw new IllegalStateException("Service order code was not issued for the appointment workshop");

        var maybeOrder = orderRepository
                .findByCodeValueAndWorkshop_WorkshopId(code.value(), code.issuedByWorkshopId());

        var order = maybeOrder
                .orElseThrow(() -> new IllegalArgumentException("Service order not found for provided code"));

        appointment.updateServiceOrder(order, code);

        var saved = repository.save(appointment);

        return Optional.of(saved);
    }

    // {@inheritDoc}
    @Override
    public Optional<Appointment> handle(RescheduleAppointmentCommand command) {
        var appointment = repository.findById(command.appointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        var existingAppointments = repository
                .findByServiceOrderWorkshop(appointment.getWorkshop());

        var hasOverlap = existingAppointments.stream()
                .filter(a -> !a.getId().equals(appointment.getId()))
                .filter(a -> !AppointmentStatus.CANCELLED.equals(a.getStatus()))
                .anyMatch(a -> a.getScheduledAt().overlapsWith(command.slot()));

        if (hasOverlap)
            throw new IllegalStateException("New appointment slot overlaps with existing appointment");

        appointment.reschedule(command.slot());

        var saved = repository.save(appointment);

        return Optional.of(saved);
    }

    // {@inheritDoc}
    @Override
    public Optional<Appointment> handle(UpdateAppointmentStatusCommand command) {
        var appointment = repository.findById(command.appointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        var updated = appointment.updateAppointmentStatus(command.status());

        if (!updated)
            throw new IllegalStateException("Appointment status is already " + command.status() + " or transition is invalid.");

        var saved = repository.save(appointment);

        return Optional.of(saved);
    }

    // {@inheritDoc}
    @Override
    public void handle(AddAppointmentNoteCommand command) {
        var appointment = repository.findById(command.appointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        appointment.addNote(command.content(), command.authorId());

        repository.save(appointment);
    }

    /**
     * Finds an existing open service order for the given workshop, vehicle, and
     * driver.
     * 
     * @param workshopId the workshop identifier
     * @param vehicleId  the vehicle identifier
     * @param driverId   the driver identifier
     * @return the existing or newly created service order
     */
    private ServiceOrder findOrCreateServiceOrder(WorkshopId workshopId, VehicleId vehicleId, DriverId driverId) {
        var existingOrders = orderRepository.findByWorkshopAndStatus(workshopId, ServiceOrderStatus.OPEN);

        for (var order : existingOrders) {
            if (order.getVehicle().equals(vehicleId) && order.getDriver().equals(driverId))
                return order;
        }

        var serviceOrderCode = new ServiceOrderCode("SO-" + System.currentTimeMillis(),
                workshopId.workshopId());
                
        var newServiceOrder = new ServiceOrder(
                new OpenServiceOrderCommand(workshopId, vehicleId, driverId, serviceOrderCode, null));

        return orderRepository.save(newServiceOrder);
    }
}