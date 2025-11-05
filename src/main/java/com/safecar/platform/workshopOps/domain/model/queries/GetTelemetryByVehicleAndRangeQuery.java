package com.safecar.platform.workshopOps.domain.model.queries;

import com.safecar.platform.workshopOps.domain.model.valueobjects.VehicleId;
import java.time.Instant;

/**
 * Query to fetch telemetry samples for a vehicle within a time range.
 */
public record GetTelemetryByVehicleAndRangeQuery(
        VehicleId vehicleId,
        Instant from,
        Instant to
) {
}
