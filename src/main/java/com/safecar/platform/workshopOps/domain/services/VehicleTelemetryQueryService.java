package com.safecar.platform.workshopOps.domain.services;

import com.safecar.platform.workshopOps.domain.model.entities.TelemetryRecord;
import com.safecar.platform.workshopOps.domain.model.queries.GetTelemetryByVehicleAndRangeQuery;
import com.safecar.platform.workshopOps.domain.model.queries.GetTelemetryRecordByIdQuery;
import com.safecar.platform.workshopOps.domain.model.queries.GetTelemetryAlertsBySeverityQuery;

import java.util.List;
import java.util.Optional;

public interface VehicleTelemetryQueryService {
    Optional<TelemetryRecord> handle(GetTelemetryRecordByIdQuery query);
    List<TelemetryRecord> handle(GetTelemetryByVehicleAndRangeQuery query);
    List<TelemetryRecord> handle(GetTelemetryAlertsBySeverityQuery query);
}
