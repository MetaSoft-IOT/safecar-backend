package com.safecar.platform.workshopOps.application.internal.commandservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safecar.platform.workshopOps.application.internal.outboundservices.acl.ExternalDeviceService;
import com.safecar.platform.workshopOps.domain.model.commands.IngestTelemetrySampleCommand;
import com.safecar.platform.workshopOps.domain.model.commands.FlushTelemetryCommand;
import com.safecar.platform.workshopOps.domain.services.VehicleTelemetryCommandService;
import com.safecar.platform.workshopOps.infrastructure.persistence.jpa.repositories.TelemetryRecordRepository;
import com.safecar.platform.workshopOps.domain.model.entities.TelemetryRecord;

import java.time.Instant;

/**
 * Enhanced command service implementation that persists TelemetryRecord entities
 * with vehicle validation through the ExternalDeviceService ACL.
 *
 * Note: This implementation persists TelemetryRecord directly via the
 * TelemetryRecordRepository (instead of instantiating the VehicleTelemetry
 * aggregate) to keep behavior simple and ensure records are stored.
 */
@Service
public class VehicleTelemetryCommandServiceImpl implements VehicleTelemetryCommandService {

    private static final Logger logger = LoggerFactory.getLogger(VehicleTelemetryCommandServiceImpl.class);

    private final TelemetryRecordRepository telemetryRecordRepository;
    private final ExternalDeviceService externalDeviceService;

    public VehicleTelemetryCommandServiceImpl(TelemetryRecordRepository telemetryRecordRepository,
                                            ExternalDeviceService externalDeviceService) {
        this.telemetryRecordRepository = telemetryRecordRepository;
        this.externalDeviceService = externalDeviceService;
    }

    @Override
    @Transactional
    public void handle(IngestTelemetrySampleCommand command) {
        logger.info("Processing telemetry ingestion command for vehicle: {}", command.sample().vehicleId());
        
        var sample = command.sample();
        
        try {
            // Validate that the vehicle exists in the Devices context before ingesting telemetry
            externalDeviceService.validateVehicleExists(sample.vehicleId());
            logger.debug("Vehicle validation successful for vehicleId: {}", sample.vehicleId());
            
            var now = Instant.now();
            var record = new TelemetryRecord(sample, now);
            telemetryRecordRepository.save(record);
            
            logger.info("Successfully ingested telemetry record for vehicle: {}", sample.vehicleId());
            // Events are not published here — aggregate-level eventing can be added later
            
        } catch (Exception e) {
            logger.error("Failed to ingest telemetry for vehicle: {} - Error: {}", 
                        sample.vehicleId(), e.getMessage());
            throw e; // Re-throw to maintain transactional behavior
        }
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
