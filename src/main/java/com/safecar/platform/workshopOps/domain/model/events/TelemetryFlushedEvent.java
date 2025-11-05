package com.safecar.platform.workshopOps.domain.model.events;

import java.time.Instant;

public record TelemetryFlushedEvent(
        Long telemetryAggregateId,
        Instant flushedAt,
        long count
) {
}
