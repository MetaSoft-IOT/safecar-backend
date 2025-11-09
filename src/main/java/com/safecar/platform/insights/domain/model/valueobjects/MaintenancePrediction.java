package com.safecar.platform.insights.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Encapsulates predictive maintenance feedback provided by the LLM.
 */
@Embeddable
public record MaintenancePrediction(

        @Column(name = "maintenance_risk_level", length = 40)
        String riskLevel,

        @Column(name = "maintenance_summary", columnDefinition = "TEXT")
        String summary,

        @Column(name = "maintenance_window", length = 120)
        String recommendedWindow
) {
}
