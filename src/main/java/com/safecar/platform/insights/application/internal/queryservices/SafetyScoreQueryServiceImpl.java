package com.safecar.platform.insights.application.internal.queryservices;

import com.safecar.platform.insights.domain.model.aggregates.SafetyScore;
import com.safecar.platform.insights.domain.model.queries.GetLatestSafetyScoreQuery;
import com.safecar.platform.insights.domain.services.SafetyScoreQueryService;
import com.safecar.platform.insights.infrastructure.persistence.jpa.repositories.SafetyScoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SafetyScoreQueryServiceImpl implements SafetyScoreQueryService {
  private final SafetyScoreRepository repo;
  public SafetyScoreQueryServiceImpl(SafetyScoreRepository repo){ this.repo = repo; }
  @Override
  public java.util.Optional<com.safecar.platform.insights.domain.model.aggregates.SafetyScore>
  handle(GetLatestSafetyScoreQuery q) {
    return repo.findTopByDriverIdAndVehicleIdOrderByCreatedAtDesc(q.driverId(), q.vehicleId());
  }
}
