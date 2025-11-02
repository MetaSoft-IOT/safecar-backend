package com.safecar.platform.appointments.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Resource for creating a new appointment.
 *
 * @param code the appointment code
 * @param scheduledDate the scheduled date and time
 * @param serviceType the type of service
 * @param description the appointment description
 * @param customerId the customer identifier
 * @param vehicleId the vehicle identifier
 * @param workshopId the workshop identifier
 */
public record CreateAppointmentResource(
        @NotBlank(message = "Code is required")
        String code,

        @NotNull(message = "Scheduled date is required")
        LocalDateTime scheduledDate,

        @NotBlank(message = "Service type is required")
        String serviceType,

        String description,

        @NotNull(message = "Customer ID is required")
        UUID customerId,

        @NotNull(message = "Vehicle ID is required")
        UUID vehicleId,

        @NotNull(message = "Workshop ID is required")
        UUID workshopId
) {
}

