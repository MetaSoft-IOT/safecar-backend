package com.safecar.platform.appointments.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Resource for updating appointment information.
 *
 * @param serviceType the type of service
 * @param description the appointment description
 */
public record UpdateAppointmentInformationResource(
        @NotBlank(message = "Service type is required")
        String serviceType,

        String description
) {
}

