package com.safecar.platform.insights.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.insights.domain.model.aggregates.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
  List<Recommendation> findByDriverIdAndVehicleIdOrderByCreatedAtDesc(Long driverId, Long vehicleId);
}
