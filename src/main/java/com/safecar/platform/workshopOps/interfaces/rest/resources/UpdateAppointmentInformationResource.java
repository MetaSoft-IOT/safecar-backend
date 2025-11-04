package com.safecar.platform.workshopOps.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Update Appointment Information Resource - Represents the data required to
 * update an appointment's information.
 * 
 * @param serviceType The type of service for the appointment (required).
 * @param description Additional description or notes for the appointment
 *                    (optional).
 */
public record UpdateAppointmentInformationResource(
                @NotBlank(message = "Service type is required") String serviceType,
                String description) {
}
