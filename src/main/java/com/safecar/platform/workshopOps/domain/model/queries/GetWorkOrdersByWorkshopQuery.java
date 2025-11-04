package com.safecar.platform.workshopOps.domain.model.queries;

import com.safecar.platform.workshopOps.domain.model.valueobjects.WorkshopId;
import com.safecar.platform.workshopOps.domain.model.valueobjects.WorkOrderStatus;

/**
 * Get Work Orders by Workshop Query - Query to get work orders for a workshop,
 * optional status.
 * 
 * @param workshopId the id of the workshop
 * @param status     the status of the work orders to filter by (optional)
 */
public record GetWorkOrdersByWorkshopQuery(
        WorkshopId workshopId,
        WorkOrderStatus status
) {
}
