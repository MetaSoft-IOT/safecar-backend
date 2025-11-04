package com.safecar.platform.workshopOps.domain.services;

import com.safecar.platform.workshopOps.domain.model.commands.FlushTelemetryCommand;
import com.safecar.platform.workshopOps.domain.model.commands.IngestTelemetrySampleCommand;

public interface VehicleTelemetryCommandService {
    void handle(IngestTelemetrySampleCommand command);
    long handle(FlushTelemetryCommand command);
}
