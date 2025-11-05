package com.safecar.platform.workshop.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

/**
 * Reschedule Appointment Resource - Represents the data required to reschedule
 * an appointment.
 * 
 * @param startAt The new start time of the appointment.
 * @param endAt   The new end time of the appointment.
 */
public record RescheduleAppointmentResource(
                @NotNull(message = "Start time is required") Instant startAt,
                @NotNull(message = "End time is required") Instant endAt) {
}
