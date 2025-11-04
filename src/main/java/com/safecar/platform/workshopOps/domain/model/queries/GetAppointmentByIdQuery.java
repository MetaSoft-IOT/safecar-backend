package com.safecar.platform.workshopOps.domain.model.queries;

/**
 * Query to get an appointment by its ID.
 */
public record GetAppointmentByIdQuery(
        Long appointmentId
) {
}

