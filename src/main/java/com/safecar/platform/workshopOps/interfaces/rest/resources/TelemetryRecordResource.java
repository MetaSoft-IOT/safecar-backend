package com.safecar.platform.workshopOps.interfaces.rest.resources;

import com.safecar.platform.workshopOps.domain.model.valueobjects.TelemetrySample;
import java.time.Instant;

/**
 * Simple resource representation for a telemetry record returned by the API.
 */
public record TelemetryRecordResource(
        Long id,
        TelemetrySample sample,
        Instant ingestedAt
) {
}
