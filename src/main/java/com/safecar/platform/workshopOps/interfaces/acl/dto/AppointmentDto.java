package com.safecar.platform.workshopOps.interfaces.acl.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Simple DTO for appointment information exposed to other bounded contexts.
 */
public record AppointmentDto(
        UUID id,
        String code,
        LocalDateTime scheduledDate,
        String status,
        UUID customerId,
        UUID vehicleId,
        UUID workshopId
) {
}

