package com.safecar.platform.workshopOps.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safecar.platform.workshopOps.domain.model.commands.IngestTelemetrySampleCommand;
import com.safecar.platform.workshopOps.domain.model.commands.FlushTelemetryCommand;
import com.safecar.platform.workshopOps.domain.services.VehicleTelemetryCommandService;
import com.safecar.platform.workshopOps.infrastructure.persistence.jpa.repositories.TelemetryRecordRepository;
import com.safecar.platform.workshopOps.domain.model.entities.TelemetryRecord;

import java.time.Instant;

/**
 * Simple command service implementation that persists TelemetryRecord entities.
 *
 * Note: This implementation persists TelemetryRecord directly via the
 * TelemetryRecordRepository (instead of instantiating the VehicleTelemetry
 * aggregate) to keep behavior simple and ensure records are stored.
 */
@Service
public class VehicleTelemetryCommandServiceImpl implements VehicleTelemetryCommandService {

    private final TelemetryRecordRepository telemetryRecordRepository;

    public VehicleTelemetryCommandServiceImpl(TelemetryRecordRepository telemetryRecordRepository) {
        this.telemetryRecordRepository = telemetryRecordRepository;
    }

    @Override
    @Transactional
    public void handle(IngestTelemetrySampleCommand command) {
        var sample = command.sample();
        var now = Instant.now();
        var record = new TelemetryRecord(sample, now);
        telemetryRecordRepository.save(record);
        // Events are not published here — aggregate-level eventing can be added later
    }

    @Override
    @Transactional
    public long handle(FlushTelemetryCommand command) {
        var telemetryAggregateId = command.telemetryAggregateId();
        var count = telemetryRecordRepository.countByTelemetryAggregateId(telemetryAggregateId);
        if (count > 0) {
            telemetryRecordRepository.deleteByTelemetryAggregateId(telemetryAggregateId);
        }
        return count;
    }
}
