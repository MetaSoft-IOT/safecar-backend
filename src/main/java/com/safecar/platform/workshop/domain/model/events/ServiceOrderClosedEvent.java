package com.safecar.platform.workshop.domain.model.events;

import java.time.Instant;

/**
 * Service Order Closed Event - Event fired when a service order is closed.
 * 
 * @param serviceOrderId the id of the service order
 * @param closedAt       the timestamp when the service order was closed
 */
public record ServiceOrderClosedEvent(
        Long serviceOrderId,
        Instant closedAt) {
}
