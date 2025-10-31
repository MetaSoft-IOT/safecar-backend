package com.safecar.platform.insights.domain.services;

import com.safecar.platform.insights.domain.model.aggregates.Recommendation;
import com.safecar.platform.insights.domain.model.commands.GenerateRecommendationsCommand;

import java.util.List;

public interface RecommendationCommandService {
  List<Recommendation> handle(GenerateRecommendationsCommand command, boolean sendToAlerts);
}
