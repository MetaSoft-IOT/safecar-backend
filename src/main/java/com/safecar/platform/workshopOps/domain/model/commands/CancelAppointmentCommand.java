package com.safecar.platform.workshopOps.domain.model.commands;

/**
 * Command to cancel an appointment.
 */
public record CancelAppointmentCommand(
        Long appointmentId
) {
}

