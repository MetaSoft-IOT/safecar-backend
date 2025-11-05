package com.safecar.platform.workshop.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

/**
 * Create Appointment Resource - Represents the data required to create a new
 * appointment.
 * 
 * @param workshopId The ID of the workshop where the appointment is to be
 *                   created.
 * @param vehicleId  The ID of the vehicle for which the appointment is being
 *                   made.
 * @param driverId   The ID of the driver associated with the appointment.
 * @param startAt    The start time of the appointment.
 * @param endAt      The end time of the appointment.
 */
public record CreateAppointmentResource(
        @NotNull(message = "Workshop ID is required") Long workshopId,
        @NotNull(message = "Vehicle ID is required") Long vehicleId,
        @NotNull(message = "Driver ID is required") Long driverId,
        @NotNull(message = "Start time is required") Instant startAt,
        @NotNull(message = "End time is required") Instant endAt) {
}
