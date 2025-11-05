package com.safecar.platform.workshopOps.domain.model.commands;

/**
 * Command to add a note to a workshop appointment.
 */
public record AddAppointmentNoteCommand(
        Long appointmentId,
        String content,
        Long authorId
) {
}
