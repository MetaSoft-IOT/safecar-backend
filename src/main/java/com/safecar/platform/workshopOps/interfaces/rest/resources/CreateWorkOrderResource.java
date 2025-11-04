package com.safecar.platform.workshopOps.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

/**
 * Create Work Order Resource - Resource to create a work order.
 */
public record CreateWorkOrderResource(
        @NotNull(message = "Workshop ID is required") Long workshopId,
        @NotNull(message = "Vehicle ID is required") Long vehicleId,
        @NotNull(message = "Driver ID is required") Long driverId,
        @NotNull(message = "Work order code is required") String code,
        Instant openedAt) {
}
