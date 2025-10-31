package com.safecar.platform.insights.domain.model.aggregates;

import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.safecar.platform.insights.domain.model.valueobjects.ScoreValue;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Entity
@Table(name = "safety_scores", indexes = {
  @Index(name="idx_safety_score_driver", columnList = "driver_id"),
  @Index(name="idx_safety_score_vehicle", columnList = "vehicle_id")
})
public class SafetyScore extends AuditableAbstractAggregateRoot<SafetyScore> {

  @NotNull @Column(name = "driver_id", nullable = false)
  private Long driverId;

  @NotNull @Column(name = "vehicle_id", nullable = false)
  private Long vehicleId;

  @Embedded
  private ScoreValue value;

  @Column(name = "explanation", length = 2000)
  private String explanation;

  protected SafetyScore() {}

  public SafetyScore(Long driverId, Long vehicleId, ScoreValue value, String explanation) {
    this.driverId = driverId;
    this.vehicleId = vehicleId;
    this.value = value;
    this.explanation = explanation;
  }

  public void update(ScoreValue newValue, String newExplanation) {
    this.value = newValue;
    this.explanation = newExplanation;
  }
}
