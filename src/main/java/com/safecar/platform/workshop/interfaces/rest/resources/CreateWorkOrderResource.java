package com.safecar.platform.workshop.interfaces.rest.resources;

import java.time.Instant;

/**
 * Create Work Order Resource - Resource to create a work order.
 * Raw data will be validated when mapping to domain commands.
 */
public record CreateWorkOrderResource(
        Long workshopId,
        Long vehicleId,
        Long driverId,
        String code,
        Instant openedAt) {
}
