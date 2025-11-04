package com.safecar.platform.workshopOps.application.internal.commandservices;

import org.springframework.stereotype.Service;

import com.safecar.platform.workshopOps.domain.model.aggregates.WorkshopAppointment;
import com.safecar.platform.workshopOps.domain.model.commands.*;
import com.safecar.platform.workshopOps.domain.model.valueobjects.AppointmentStatus;
import com.safecar.platform.workshopOps.infrastructure.persistence.jpa.repositories.WorkshopAppointmentRepository;
import com.safecar.platform.workshopOps.infrastructure.persistence.jpa.repositories.WorkshopOrderRepository;
import com.safecar.platform.workshopOps.domain.services.WorkshopAppointmentCommandService;

/**
 * Workshop Appointment Command Service Implementation
 * 
 * @see WorkshopAppointmentCommandService WorkshopAppointmentCommandService for
 *      method details.
 */
@Service
public class WorkshopAppointmentCommandServiceImpl implements WorkshopAppointmentCommandService {

    /**
     * Repository for WorkshopAppointment to handle persistence operations.
     */
    private final WorkshopAppointmentRepository repository;
    private final WorkshopOrderRepository orderRepository;

    /**
     * Constructor for WorkshopAppointmentCommandServiceImpl.
     * 
     * @param repository Repository for WorkshopAppointment.
     */
    public WorkshopAppointmentCommandServiceImpl(WorkshopAppointmentRepository repository, WorkshopOrderRepository orderRepository) {
        this.repository = repository;
        this.orderRepository = orderRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(CreateAppointmentCommand command) {

        var existingAppointments = repository.findByWorkshop(command.workshopId());

        var hasOverlap = existingAppointments.stream()
                .filter(appointment -> !AppointmentStatus.CANCELLED.equals(appointment.getStatus()))
                .anyMatch(appointment -> appointment.getScheduledAt().overlapsWith(command.slot()));

        if (hasOverlap)
            throw new IllegalStateException("Appointment slot overlaps with existing appointment");

        var appointment = new WorkshopAppointment(command);
        repository.save(appointment);
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

        appointment.linkToWorkOrder(order.getId(), code);
        repository.save(appointment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(RescheduleAppointmentCommand command) {
        var appointment = repository.findById(command.appointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        var existingAppointments = repository.findByWorkshop(appointment.getWorkshop());

        var hasOverlap = existingAppointments.stream()
                .filter(a -> !a.getId().equals(appointment.getId()))
                .filter(a -> !AppointmentStatus.CANCELLED.equals(a.getStatus()))
                .anyMatch(a -> a.getScheduledAt().overlapsWith(command.slot()));

        if (hasOverlap)
            throw new IllegalStateException("New appointment slot overlaps with existing appointment");

        appointment.reschedule(command.slot());
        repository.save(appointment);
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
}