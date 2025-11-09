package com.safecar.platform.insights.domain.model.valueobjects;

import java.time.Instant;
import java.util.List;

/**
 * Result returned by the analytics gateway (OpenAI).
 */
public record TelemetryInsightResult(
        String riskLevel,
        String maintenanceSummary,
        String maintenanceWindow,
        Integer drivingHabitScore,
        String drivingHabitSummary,
        String drivingAlerts,
        List<InsightRecommendation> recommendations,
        String llmResponseId,
        Instant generatedAt
) {
}
