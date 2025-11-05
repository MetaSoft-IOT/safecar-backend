package com.safecar.platform.workshop.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safecar.platform.workshop.domain.model.commands.FlushTelemetryCommand;
import com.safecar.platform.workshop.domain.model.commands.IngestTelemetrySampleCommand;
import com.safecar.platform.workshop.domain.model.queries.GetTelemetryAlertsBySeverityQuery;
import com.safecar.platform.workshop.domain.model.queries.GetTelemetryByVehicleAndRangeQuery;
import com.safecar.platform.workshop.domain.model.queries.GetTelemetryRecordByIdQuery;
import com.safecar.platform.workshop.domain.services.VehicleTelemetryCommandService;
import com.safecar.platform.workshop.domain.services.VehicleTelemetryQueryService;
import com.safecar.platform.workshop.interfaces.rest.resources.TelemetryRecordResource;
import com.safecar.platform.workshop.interfaces.rest.transform.TelemetryRecordResourceFromEntityAssembler;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for handling vehicle telemetry operations.
 */
@RestController
@RequestMapping(value = "/api/v1/telemetry", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Telemetry", description = "Vehicle telemetry endpoints")
public class WorkshopOpsTelemetryController {

    /**
     * Command and Query services for vehicle telemetry.
     */
    private final VehicleTelemetryCommandService commandService;
    private final VehicleTelemetryQueryService queryService;

    /**
     * Ingest a telemetry sample.
     */
    @PostMapping(value = "/ingest", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Ingest a telemetry sample")
    public ResponseEntity<Void> postIngest(@RequestBody IngestTelemetrySampleCommand command) {
        commandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /*
     * Flush telemetry records for an aggregate id.
     */
    @PostMapping(value = "/flush", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Flush telemetry records for an aggregate id")
    public ResponseEntity<Long> postFlush(@RequestBody FlushTelemetryCommand command) {
        var count = commandService.handle(command);
        return ResponseEntity.ok(count);
    }

    /*
     * Get telemetry record by id.
     */
    @GetMapping(value = "/records/{id}")
    @Operation(summary = "Get telemetry record by id")
    public ResponseEntity<TelemetryRecordResource> getRecordById(@PathVariable Long id) {
        var query = new GetTelemetryRecordByIdQuery(id);
        var maybe = queryService.handle(query);
        if (maybe.isEmpty())
            return ResponseEntity.notFound().build();
        var resource = TelemetryRecordResourceFromEntityAssembler.toResourceFromEntity(maybe.get());
        return ResponseEntity.ok(resource);
    }

    /**
     * Get telemetry records for vehicle in range.
     * 
     * @param vehicleId   The ID of the vehicle.
     * @param plateNumber The plate number of the vehicle.
     * @param from        The start time of the range.
     * @param to          The end time of the range.
     * @return A list of telemetry records for the vehicle in the specified range.
     */
    @GetMapping(value = "/by-vehicle")
    @Operation(summary = "Get telemetry records for vehicle in range")
    public ResponseEntity<List<TelemetryRecordResource>> getByVehicleAndRange(@RequestParam Long vehicleId,
            @RequestParam String plateNumber, @RequestParam Instant from, @RequestParam Instant to) {
        var vehicle = new com.safecar.platform.workshop.domain.model.valueobjects.VehicleId(vehicleId, plateNumber);
        var query = new GetTelemetryByVehicleAndRangeQuery(vehicle, from, to);
        var records = queryService.handle(query);
        var resources = records.stream().map(TelemetryRecordResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    /**
     * Get telemetry alerts by severity in range.
     * 
     * @param severity The severity level of the alerts.
     * @param from     The start time of the range.
     * @param to       The end time of the range.
     * @return A list of telemetry alerts matching the criteria.
     */
    @GetMapping(value = "/alerts")
    @Operation(summary = "Get telemetry alerts by severity in range")
    public ResponseEntity<List<TelemetryRecordResource>> getAlertsBySeverity(
            @RequestParam com.safecar.platform.workshop.domain.model.valueobjects.AlertSeverity severity,
            @RequestParam Instant from, @RequestParam Instant to) {
        var query = new GetTelemetryAlertsBySeverityQuery(severity, from, to);
        var records = queryService.handle(query);
        var resources = records.stream().map(TelemetryRecordResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }
}
