package com.safecar.platform.insights.application.internal.queryservices;

import com.safecar.platform.insights.domain.model.aggregates.Recommendation;
import com.safecar.platform.insights.domain.model.queries.GetRecommendationsQuery;
import com.safecar.platform.insights.domain.services.RecommendationQueryService;
import com.safecar.platform.insights.infrastructure.persistence.jpa.repositories.RecommendationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RecommendationQueryServiceImpl implements RecommendationQueryService {
  private final RecommendationRepository repo;
  public RecommendationQueryServiceImpl(RecommendationRepository repo){ this.repo = repo; }
  @Override
  public java.util.List<com.safecar.platform.insights.domain.model.aggregates.Recommendation>
  handle(GetRecommendationsQuery q) {
    return repo.findByDriverIdAndVehicleIdOrderByCreatedAtDesc(q.driverId(), q.vehicleId());
  }
}
