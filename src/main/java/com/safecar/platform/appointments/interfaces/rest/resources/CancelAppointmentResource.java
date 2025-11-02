package com.safecar.platform.appointments.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Resource for cancelling an appointment.
 *
 * @param reason the cancellation reason
 */
public record CancelAppointmentResource(
        @NotBlank(message = "Cancellation reason is required")
        String reason
) {
}

