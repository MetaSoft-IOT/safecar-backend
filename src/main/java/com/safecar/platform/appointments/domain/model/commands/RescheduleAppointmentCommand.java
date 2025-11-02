package com.safecar.platform.appointments.domain.model.commands;

import java.time.LocalDateTime;
import java.util.UUID;

public record RescheduleAppointmentCommand(UUID appointmentId, LocalDateTime newScheduledDate) {
}

