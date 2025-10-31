package com.safecar.platform.insights.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.insights.domain.model.aggregates.SafetyScore;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SafetyScoreRepository extends JpaRepository<SafetyScore, Long> {
  Optional<SafetyScore> findTopByDriverIdAndVehicleIdOrderByCreatedAtDesc(Long driverId, Long vehicleId);
}
