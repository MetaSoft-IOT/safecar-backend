package com.safecar.platform.insights.interfaces.rest.transform;

import com.safecar.platform.insights.domain.model.commands.GenerateVehicleInsightCommand;
import com.safecar.platform.insights.domain.model.valueobjects.TelemetrySensorPayload;
import com.safecar.platform.insights.domain.model.valueobjects.VehicleReference;
import com.safecar.platform.insights.interfaces.rest.resources.GenerateVehicleInsightResource;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

public class GenerateVehicleInsightCommandFromResourceAssembler {

    public static GenerateVehicleInsightCommand toCommand(GenerateVehicleInsightResource resource) {
        var vehicle = new VehicleReference(
                resource.driverId(),
                resource.driverFullName(),
                resource.vehicleId(),
                resource.plateNumber()
        );

        var payload = new TelemetrySensorPayload(
                resource.capturedAt() != null ? resource.capturedAt() : Instant.now(),
                resource.speedKmh(),
                mapTirePressure(resource.tirePressure()),
                resource.cabinGas() != null ? resource.cabinGas().ppm() : null,
                resource.cabinGas() != null ? resource.cabinGas().type() : null,
                resource.location() != null ? resource.location().latitude() : null,
                resource.location() != null ? resource.location().longitude() : null,
                resource.acceleration() != null ? resource.acceleration().lateralG() : null,
                resource.acceleration() != null ? resource.acceleration().longitudinalG() : null,
                resource.acceleration() != null ? resource.acceleration().verticalG() : null,
                resource.severity()
        );
        return new GenerateVehicleInsightCommand(vehicle, payload);
    }

    private static Map<String, BigDecimal> mapTirePressure(GenerateVehicleInsightResource.TirePressureResource resource) {
        if (resource == null) return null;
        var map = new LinkedHashMap<String, BigDecimal>();
        map.put("front_left", resource.frontLeft());
        map.put("front_right", resource.frontRight());
        map.put("rear_left", resource.rearLeft());
        map.put("rear_right", resource.rearRight());
        return map;
    }
}
