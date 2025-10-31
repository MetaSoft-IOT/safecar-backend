package com.safecar.platform.insights.domain.services;

import com.safecar.platform.insights.domain.model.aggregates.SafetyScore;
import com.safecar.platform.insights.domain.model.commands.ComputeSafetyScoreCommand;

import java.util.Optional;

public interface SafetyScoreCommandService {
  Optional<SafetyScore> handle(ComputeSafetyScoreCommand command);
}
