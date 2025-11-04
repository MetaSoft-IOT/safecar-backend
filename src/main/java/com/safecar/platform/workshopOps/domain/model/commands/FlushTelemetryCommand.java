package com.safecar.platform.workshopOps.domain.model.commands;

/**
 * Command to flush telemetry aggregate by id.
 */
public record FlushTelemetryCommand(
        Long telemetryAggregateId
) {
}
