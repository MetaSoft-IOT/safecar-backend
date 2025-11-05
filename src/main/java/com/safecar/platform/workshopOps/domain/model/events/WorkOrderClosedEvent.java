package com.safecar.platform.workshopOps.domain.model.events;

import java.time.Instant;

/**
 * Work Order Closed Event - Event fired when a work order is closed.
 * 
 * @param workOrderId the id of the work order
 * @param closedAt    the timestamp when the work order was closed
 */
public record WorkOrderClosedEvent(
        Long workOrderId,
        Instant closedAt) {
}
