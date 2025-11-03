package com.safecar.platform.workshopOps.domain.model.commands;

import java.time.LocalDateTime;
import java.util.UUID;

public record RescheduleAppointmentCommand(UUID appointmentId, LocalDateTime newScheduledDate) {
}

