package com.safecar.platform.workshopOps.domain.model.queries;

import com.safecar.platform.workshopOps.domain.model.valueobjects.AlertSeverity;
import java.time.Instant;

/**
 * Query to fetch telemetry alerts filtered by severity in a time range.
 */
public record GetTelemetryAlertsBySeverityQuery(
        AlertSeverity severity,
        Instant from,
        Instant to
) {
}
