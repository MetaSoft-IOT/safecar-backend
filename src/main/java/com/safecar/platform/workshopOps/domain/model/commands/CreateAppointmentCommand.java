package com.safecar.platform.workshopOps.domain.model.commands;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateAppointmentCommand(
        String code,
        LocalDateTime scheduledDate,
        String serviceType,
        String description,
        UUID customerId,
        UUID vehicleId,
        UUID workshopId
) {
}

