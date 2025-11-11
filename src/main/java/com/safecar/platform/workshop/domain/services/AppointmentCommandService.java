package com.safecar.platform.workshop.domain.services;

import java.util.Optional;

import com.safecar.platform.workshop.domain.model.aggregates.Appointment;
import com.safecar.platform.workshop.domain.model.commands.*;

/**
 * Appointment Command Service
 * <p>
 * Handles commands related to workshop appointment operations.
 * </p>
 */
public interface AppointmentCommandService {
    /**
     * Handles the creation of a new workshop appointment.
     * 
     * @param command The {@link CreateAppointmentCommand} command containing
     *                appointment details
     */
    Optional<Appointment> handle(CreateAppointmentCommand command);

    /**
     * Handles linking an appointment to a service order.
     * 
     * @param command The {@link LinkAppointmentToServiceOrderCommand} command
     *                containing linking details
     */
    Optional<Appointment> handle(LinkAppointmentToServiceOrderCommand command);

    /**
     * Handles rescheduling an existing appointment.
     * 
     * @param command The {@link RescheduleAppointmentCommand} command containing
     *                rescheduling details
     */
    Optional<Appointment> handle(RescheduleAppointmentCommand command);

    /**
     * Handles updating appointment status (unified state transitions).
     * 
     * @param command The {@link UpdateAppointmentStatusCommand} command containing
     *                target status
     */
    Optional<Appointment> handle(UpdateAppointmentStatusCommand command);

    /**
     * Handles adding a note to an existing appointment.
     * 
     * @param command The {@link AddAppointmentNoteCommand} command containing note
     *                details
     */
    void handle(AddAppointmentNoteCommand command);
}
