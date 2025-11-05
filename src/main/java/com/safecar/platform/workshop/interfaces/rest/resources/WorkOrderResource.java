package com.safecar.platform.workshop.interfaces.rest.resources;

import java.time.Instant;

/**
 * Work Order Resource - Resource representing a work order.
 */
public record WorkOrderResource(
        Long id,
        String code,
        Long workshopId,
        Long vehicleId,
        Long driverId,
        Instant openedAt,
        Instant closedAt,
        String status) {
}
