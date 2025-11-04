package com.safecar.platform.workshopOps.interfaces.rest.resources;

/**
 * Cancel Appointment Resource - Represents the data required to cancel an
 * appointment.
 * 
 * @param reason The reason for cancellation (optional).
 */
public record CancelAppointmentResource(
                String reason) {
}
