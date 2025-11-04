package com.safecar.platform.workshopOps.domain.model.queries;

/**
 * Get Work Order by ID Query - Query to get a work order by id.
 * 
 * @param workOrderId the id of the work order
 */
public record GetWorkOrderByIdQuery(
        Long workOrderId) {
}
