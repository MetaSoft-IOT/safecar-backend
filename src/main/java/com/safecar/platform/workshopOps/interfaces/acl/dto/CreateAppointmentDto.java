package com.safecar.platform.workshopOps.interfaces.acl.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Simple DTO for creating appointments from other bounded contexts.
 * <p>
 * This is a decoupled representation to avoid exposing internal domain details.
 * </p>
 */
public record CreateAppointmentDto(
        String code,
        LocalDateTime scheduledDate,
        String serviceType,
        String description,
        UUID customerId,
        UUID vehicleId,
        UUID workshopId
) {
}

