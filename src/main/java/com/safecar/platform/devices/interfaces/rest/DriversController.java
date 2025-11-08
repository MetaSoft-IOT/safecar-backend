package com.safecar.platform.devices.interfaces.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safecar.platform.devices.domain.services.DriverCommandService;
import com.safecar.platform.devices.interfaces.rest.resources.DriverResource;
import com.safecar.platform.devices.interfaces.rest.resources.UpdateDriverMetricsResource;
import com.safecar.platform.devices.interfaces.rest.transform.DriverResourceFromEntityAssembler;
import com.safecar.platform.devices.interfaces.rest.transform.UpdateDriverMetricsCommandFromResourceAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/api/v1/driver-profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Driver Profiles", description = "Driver profile management endpoints")
public class DriversController {

    private final DriverCommandService commandService;

    public DriversController(DriverCommandService commandService) {
        this.commandService = commandService;
    }

    @Operation(summary = "Update Driver Metrics by Profile ID")
    @PatchMapping("/{profileId}/metrics")
    @ApiResponses(value = {
    })
    public ResponseEntity<DriverResource> updateDriverMetrics(
            @PathVariable Long profileId,
            @RequestBody UpdateDriverMetricsResource resource) {
        var command = UpdateDriverMetricsCommandFromResourceAssembler.toCommandFromResource(resource);
        var driverOpt = commandService.handle(command, profileId);

        if (driverOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var driver = driverOpt.get();
        var driverResource = DriverResourceFromEntityAssembler.toResourceFromEntity(driver);
        return ResponseEntity.ok(driverResource);
    }
}
