package com.safecar.platform.workshop.domain.model.queries;

import com.safecar.platform.workshop.domain.model.valueobjects.ServiceOrderStatus;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;

/**
 * Query to get service orders by workshop, optionally filtered by status.
 * 
 * @param workshopId the ID of the workshop
 * @param status optional status filter (null means all statuses)
 */
public record GetServiceOrdersByWorkshopQuery(WorkshopId workshopId, ServiceOrderStatus status) {
}
