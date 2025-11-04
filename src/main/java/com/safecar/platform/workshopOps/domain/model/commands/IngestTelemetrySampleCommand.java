package com.safecar.platform.workshopOps.domain.model.commands;

import com.safecar.platform.workshopOps.domain.model.valueobjects.TelemetrySample;

/**
 * Command to ingest a telemetry sample.
 */
public record IngestTelemetrySampleCommand(
        TelemetrySample sample
) {
}
