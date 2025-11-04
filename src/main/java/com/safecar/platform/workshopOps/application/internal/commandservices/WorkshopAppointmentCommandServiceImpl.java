package com.safecar.platform.workshopOps.application.internal.commandservices;

import org.springframework.stereotype.Service;

import com.safecar.platform.workshopOps.domain.model.aggregates.WorkshopAppointment;
import com.safecar.platform.workshopOps.domain.model.commands.*;
import com.safecar.platform.workshopOps.domain.model.valueobjects.AppointmentStatus;
import com.safecar.platform.workshopOps.infrastructure.persistence.jpa.repositories.WorkshopAppointmentRepository;
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

    /**
     * Constructor for WorkshopAppointmentCommandServiceImpl.
     * 
     * @param repository Repository for WorkshopAppointment.
     */
    public WorkshopAppointmentCommandServiceImpl(WorkshopAppointmentRepository repository) {
        this.repository = repository;
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

        appointment.linkToWorkOrder(command.workOrderCode());
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