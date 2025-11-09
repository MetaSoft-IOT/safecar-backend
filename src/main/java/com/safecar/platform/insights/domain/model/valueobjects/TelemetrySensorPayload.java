package com.safecar.platform.insights.domain.model.valueobjects;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

/**
 * Simplified snapshot of sensors that feeds the analytics engine.
 */
public record TelemetrySensorPayload(
        Instant capturedAt,
        BigDecimal speedKmh,
        Map<String, BigDecimal> tirePressures,
        BigDecimal cabinGasPpm,
        String cabinGasType,
        BigDecimal latitude,
        BigDecimal longitude,
        BigDecimal lateralG,
        BigDecimal longitudinalG,
        BigDecimal verticalG,
        String alertSeverity
) {
}
