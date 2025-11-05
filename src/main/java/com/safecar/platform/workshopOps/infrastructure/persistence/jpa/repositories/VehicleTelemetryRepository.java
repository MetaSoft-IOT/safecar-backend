package com.safecar.platform.workshopOps.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.workshopOps.domain.model.aggregates.VehicleTelemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleTelemetryRepository extends JpaRepository<VehicleTelemetry, Long> {
}
