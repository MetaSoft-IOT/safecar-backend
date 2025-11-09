package com.safecar.platform.insights.interfaces.rest;

import com.safecar.platform.insights.domain.model.queries.GetVehicleInsightByVehicleIdQuery;
import com.safecar.platform.insights.domain.services.VehicleInsightCommandService;
import com.safecar.platform.insights.domain.services.VehicleInsightQueryService;
import com.safecar.platform.insights.interfaces.rest.resources.GenerateVehicleInsightResource;
import com.safecar.platform.insights.interfaces.rest.resources.VehicleInsightResource;
import com.safecar.platform.insights.interfaces.rest.transform.GenerateVehicleInsightCommandFromResourceAssembler;
import com.safecar.platform.insights.interfaces.rest.transform.VehicleInsightResourceFromEntityAssembler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/insights/vehicle", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class VehicleInsightsController {

    private final VehicleInsightCommandService commandService;
    private final VehicleInsightQueryService queryService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VehicleInsightResource> generateInsight(
            @RequestBody @Valid GenerateVehicleInsightResource resource
    ) {
        var command = GenerateVehicleInsightCommandFromResourceAssembler.toCommand(resource);
        var insight = commandService.handle(command);
        var response = VehicleInsightResourceFromEntityAssembler.toResource(insight);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleInsightResource> getLatestInsight(@PathVariable Long vehicleId) {
        var optional = queryService.handle(new GetVehicleInsightByVehicleIdQuery(vehicleId));
        return optional
                .map(VehicleInsightResourceFromEntityAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
