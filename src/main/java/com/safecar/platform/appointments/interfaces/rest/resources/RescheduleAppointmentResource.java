package com.safecar.platform.appointments.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Resource for rescheduling an appointment.
 *
 * @param newScheduledDate the new scheduled date and time
 */
public record RescheduleAppointmentResource(
        @NotNull(message = "New scheduled date is required")
        LocalDateTime newScheduledDate
) {
}

