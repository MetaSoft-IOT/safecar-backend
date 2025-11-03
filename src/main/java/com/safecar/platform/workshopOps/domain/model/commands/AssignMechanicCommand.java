package com.safecar.platform.workshopOps.domain.model.commands;

import java.util.UUID;

public record AssignMechanicCommand(UUID appointmentId, UUID mechanicId) {
}

