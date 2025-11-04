package com.safecar.platform.workshopOps.domain.model.queries;

/**
 * Query to get appointments by work order ID.
 */
public record GetAppointmentsByWorkOrderQuery(
        Long workOrderId
) {
}