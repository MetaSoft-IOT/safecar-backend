package com.safecar.platform.appointments.domain.model.commands;

import java.util.UUID;

public record AssignMechanicCommand(UUID appointmentId, UUID mechanicId) {
}

