package com.safecar.platform.workshopOps.domain.services;

import com.safecar.platform.workshopOps.domain.model.commands.*;

/**
 * Workshop Appointment Command Service - handles commands related to workshop appointments.
 */
public interface WorkshopAppointmentCommandService {
    /**
     * Handles the creation of a new workshop appointment.
     * @param command the command containing appointment details
     */
    void handle(CreateAppointmentCommand command);
    /**
     * Handles linking an appointment to a work order.
     * @param command the command containing linking details
     */
    void handle(LinkAppointmentToWorkOrderCommand command);
    /**
     * Handles rescheduling an existing appointment.
     * @param command the command containing rescheduling details
     */
    void handle(RescheduleAppointmentCommand command);
    /**
     * Handles cancelling an existing appointment.
     * @param command the command containing cancellation details
     */
    void handle(CancelAppointmentCommand command);
    /**
     * Handles adding a note to an existing appointment.
     * @param command the command containing note details
     */
    void handle(AddAppointmentNoteCommand command);
}

