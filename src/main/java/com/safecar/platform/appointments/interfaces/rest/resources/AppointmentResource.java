package com.safecar.platform.appointments.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Resource representing an appointment.
 * <p>
 * Used as a response object for appointment-related endpoints.
 * </p>
 *
 * @param id the unique identifier of the appointment
 * @param code the appointment code
 * @param scheduledDate the scheduled date and time
 * @param endDate the end date and time
 * @param status the appointment status
 * @param serviceType the type of service
 * @param description the appointment description
 * @param customerId the customer identifier
 * @param vehicleId the vehicle identifier
 * @param mechanicId the mechanic identifier
 * @param workshopId the workshop identifier
 * @param notes the list of appointment notes
 */
public record AppointmentResource(
        UUID id,
        String code,
        LocalDateTime scheduledDate,
        LocalDateTime endDate,
        String status,
        String serviceType,
        String description,
        UUID customerId,
        UUID vehicleId,
        UUID mechanicId,
        UUID workshopId,
        List<AppointmentNoteResource> notes
) {
}

