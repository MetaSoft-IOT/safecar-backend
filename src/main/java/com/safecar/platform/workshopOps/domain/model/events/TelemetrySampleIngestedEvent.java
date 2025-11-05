package com.safecar.platform.workshopOps.domain.model.events;

import com.safecar.platform.workshopOps.domain.model.valueobjects.TelemetrySample;
import java.time.Instant;

public record TelemetrySampleIngestedEvent(
        Long recordId,
        TelemetrySample sample,
        Instant ingestedAt
) {
}
