package com.safecar.platform.insights.application.internal.eventhandlers;

import com.safecar.platform.insights.domain.model.commands.GenerateVehicleInsightCommand;
import com.safecar.platform.insights.domain.model.valueobjects.TelemetrySensorPayload;
import com.safecar.platform.insights.domain.model.valueobjects.VehicleReference;
import com.safecar.platform.insights.domain.services.VehicleInsightCommandService;
import com.safecar.platform.workshop.domain.model.events.TelemetrySampleIngestedEvent;
import com.safecar.platform.workshop.domain.model.valueobjects.AlertSeverity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TelemetryInsightGenerationHandler {

    private final VehicleInsightCommandService commandService;

    @EventListener
    public void onTelemetrySample(TelemetrySampleIngestedEvent event) {
        if (!shouldAnalyze(event)) {
            return;
        }
        var sample = event.sample();
        var vehicle = new VehicleReference(
                sample.driverId().driverId(),
                resolveDriverFullName(sample.driverId()),
                sample.vehicleId().vehicleId(),
                resolveVehiclePlate(sample.vehicleId())
        );
        var payload = new TelemetrySensorPayload(
                event.ingestedAt(),
                sample.speed() != null ? sample.speed().value() : null,
                buildTirePressureMap(sample.tirePressure()),
                sample.cabinGasLevel() != null ? sample.cabinGasLevel().concentrationPpm() : null,
                sample.cabinGasLevel() != null ? sample.cabinGasLevel().type().name() : null,
                sample.location() != null ? sample.location().latitude() : null,
                sample.location() != null ? sample.location().longitude() : null,
                sample.accelerationVector() != null ? sample.accelerationVector().lateralG() : null,
                sample.accelerationVector() != null ? sample.accelerationVector().longitudinalG() : null,
                sample.accelerationVector() != null ? sample.accelerationVector().verticalG() : null,
                sample.severity().name()
        );

        var command = new GenerateVehicleInsightCommand(vehicle, payload);
        commandService.handle(command);
    }

    private boolean shouldAnalyze(TelemetrySampleIngestedEvent event) {
        var severity = event.sample().severity();
        if (severity == AlertSeverity.CRITICAL) {
            return true;
        }
        return Optional.ofNullable(event.sample().accelerationVector())
                .map(vec -> vec.isHarshEvent())
                .orElse(false) ||
                hasTirePressureAlert(event.sample().tirePressure()) ||
                hasGasAlert(event.sample().cabinGasLevel());
    }

    private boolean hasTirePressureAlert(com.safecar.platform.workshop.domain.model.valueobjects.TirePressure tirePressure) {
        if (tirePressure == null) return false;
        var pressures = new BigDecimal[]{
                tirePressure.frontLeft(),
                tirePressure.frontRight(),
                tirePressure.rearLeft(),
                tirePressure.rearRight()
        };
        for (BigDecimal value : pressures) {
            if (value == null) continue;
            if (value.compareTo(BigDecimal.valueOf(26)) < 0 || value.compareTo(BigDecimal.valueOf(45)) > 0) {
                return true;
            }
        }
        return false;
    }

    private boolean hasGasAlert(com.safecar.platform.workshop.domain.model.valueobjects.CabinGasLevel gasLevel) {
        if (gasLevel == null) return false;
        return gasLevel.concentrationPpm().compareTo(BigDecimal.valueOf(700)) > 0;
    }

    private Map<String, BigDecimal> buildTirePressureMap(com.safecar.platform.workshop.domain.model.valueobjects.TirePressure tp) {
        if (tp == null) return null;
        var map = new LinkedHashMap<String, BigDecimal>();
        map.put("front_left", tp.frontLeft());
        map.put("front_right", tp.frontRight());
        map.put("rear_left", tp.rearLeft());
        map.put("rear_right", tp.rearRight());
        return map;
    }

    private String resolveDriverFullName(com.safecar.platform.workshop.domain.model.valueobjects.DriverId driverId) {
        if (driverId == null || driverId.driverId() == null) {
            return "Unknown driver";
        }
        return "Driver #" + driverId.driverId();
    }

    private String resolveVehiclePlate(com.safecar.platform.workshop.domain.model.valueobjects.VehicleId vehicleId) {
        if (vehicleId == null || vehicleId.vehicleId() == null) {
            return "UNKNOWN-VEHICLE";
        }
        return "VEH-" + vehicleId.vehicleId();
    }
}
