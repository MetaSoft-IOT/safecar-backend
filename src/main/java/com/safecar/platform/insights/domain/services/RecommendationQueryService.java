package com.safecar.platform.insights.domain.services;

import com.safecar.platform.insights.domain.model.aggregates.Recommendation;
import com.safecar.platform.insights.domain.model.queries.GetRecommendationsQuery;

import java.util.List;

public interface RecommendationQueryService {
  List<Recommendation> handle(GetRecommendationsQuery query);
}
