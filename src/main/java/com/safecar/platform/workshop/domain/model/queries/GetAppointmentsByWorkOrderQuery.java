package com.safecar.platform.workshop.domain.model.queries;

/**
 * Query to get appointments by work order ID.
 */
public record GetAppointmentsByWorkOrderQuery(
        Long workOrderId
) {
}