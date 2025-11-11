package com.safecar.platform.workshop.interfaces.rest.resources;

import java.time.Instant;

/**
 * Service Order Resource - Resource representing a service order.
 */
public record ServiceOrderResource(
        Long id,
        String code,
        Long workshopId,
        Long vehicleId,
        Long driverId,
        Instant openedAt,
        Instant closedAt,
        String status) {
}
