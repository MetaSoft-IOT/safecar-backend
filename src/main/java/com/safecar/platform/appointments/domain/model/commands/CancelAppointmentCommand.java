package com.safecar.platform.appointments.domain.model.commands;

import java.util.UUID;

public record CancelAppointmentCommand(UUID appointmentId, String reason) {
}

