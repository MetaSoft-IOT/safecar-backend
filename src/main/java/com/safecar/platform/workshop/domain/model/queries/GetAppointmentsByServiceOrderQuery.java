package com.safecar.platform.workshop.domain.model.queries;

/**
 * Query to get appointments by service order ID.
 */
public record GetAppointmentsByServiceOrderQuery(
        Long serviceOrderId
) {
}