package com.safecar.platform.workshop.domain.model.queries;

/**
 * Query to get a service order by ID.
 * 
 * @param serviceOrderId the ID of the service order to retrieve
 */
public record GetServiceOrderByIdQuery(Long serviceOrderId) {
}
