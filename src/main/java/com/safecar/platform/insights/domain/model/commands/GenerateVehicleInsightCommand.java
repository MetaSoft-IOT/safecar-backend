package com.safecar.platform.insights.domain.model.commands;

import com.safecar.platform.insights.domain.model.valueobjects.TelemetrySensorPayload;
import com.safecar.platform.insights.domain.model.valueobjects.VehicleReference;

/**
 * Command to request a new insight from LLM analytics.
 */
public record GenerateVehicleInsightCommand(
        VehicleReference vehicle,
        TelemetrySensorPayload sensorPayload
) {
    public GenerateVehicleInsightCommand {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle reference is required");
        }
        if (sensorPayload == null) {
            throw new IllegalArgumentException("Sensor payload is required");
        }
    }
}
