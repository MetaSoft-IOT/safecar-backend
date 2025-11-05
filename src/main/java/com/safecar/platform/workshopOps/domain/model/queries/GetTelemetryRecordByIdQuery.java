package com.safecar.platform.workshopOps.domain.model.queries;

/**
 * Query to fetch a single telemetry record by its id.
 */
public record GetTelemetryRecordByIdQuery(
        Long recordId
) {
}
