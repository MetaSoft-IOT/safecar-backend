package com.safecar.platform.insights.domain.services;

import com.safecar.platform.insights.domain.model.aggregates.SafetyScore;
import com.safecar.platform.insights.domain.model.queries.GetLatestSafetyScoreQuery;

import java.util.Optional;

public interface SafetyScoreQueryService {
  Optional<SafetyScore> handle(GetLatestSafetyScoreQuery query);
}
