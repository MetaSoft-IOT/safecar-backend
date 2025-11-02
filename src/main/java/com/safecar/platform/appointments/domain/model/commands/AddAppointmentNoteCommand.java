package com.safecar.platform.appointments.domain.model.commands;

import java.util.UUID;

public record AddAppointmentNoteCommand(UUID appointmentId, String content, UUID authorId) {
}

