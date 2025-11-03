package com.safecar.platform.workshopOps.domain.model.commands;

import java.util.UUID;

public record CompleteAppointmentCommand(UUID appointmentId) {
}

