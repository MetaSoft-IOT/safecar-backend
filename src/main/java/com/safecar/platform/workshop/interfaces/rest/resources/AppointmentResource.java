package com.safecar.platform.workshop.interfaces.rest.resources;

import java.time.Instant;
import java.util.List;

/**
 * Appointment Resource - Represents an appointment required in the workshop
 * operations.
 * 
 * @param id          the unique identifier of the appointment
 * @param workshopId  the ID of the workshop where the appointment is scheduled
 * @param vehicleId   the ID of the vehicle associated with the appointment
 * @param driverId    the ID of the driver associated with the appointment
 * @param serviceOrderId the ID of the service order linked to the appointment
 *                    (optional)
 * @param startAt     the start time of the appointment
 * @param endAt       the end time of the appointment
 * @param status      the current status of the appointment
 * @param notes       the list of notes associated with the appointment
 */
public record AppointmentResource(
                Long id,
                Long workshopId,
                Long vehicleId,
                Long driverId,
                Long serviceOrderId,
                Instant startAt,
                Instant endAt,
                String status,
                List<AppointmentNoteResource> notes) {
}
