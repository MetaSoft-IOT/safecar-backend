package com.safecar.platform.insights.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record ScoreValue(int value) {
  public ScoreValue {
    if (value < 0 || value > 100) {
      throw new IllegalArgumentException("Safety Score must be between 0 and 100");
    }
  }
}
