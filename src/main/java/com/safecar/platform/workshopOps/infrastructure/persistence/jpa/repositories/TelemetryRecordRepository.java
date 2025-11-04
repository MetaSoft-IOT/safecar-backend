package com.safecar.platform.workshopOps.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.workshopOps.domain.model.entities.TelemetryRecord;
import com.safecar.platform.workshopOps.domain.model.valueobjects.VehicleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TelemetryRecordRepository extends JpaRepository<TelemetryRecord, Long> {
    List<TelemetryRecord> findBySampleVehicleIdAndIngestedAtBetween(VehicleId vehicleId, Instant from, Instant to);
    List<TelemetryRecord> findBySampleSeverityAndIngestedAtBetween(com.safecar.platform.workshopOps.domain.model.valueobjects.AlertSeverity severity, Instant from, Instant to);

    long countByTelemetryAggregateId(Long telemetryAggregateId);

    void deleteByTelemetryAggregateId(Long telemetryAggregateId);
}
