package com.safecar.platform.workshop.interfaces.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safecar.platform.workshop.domain.services.MechanicCommandService;
import com.safecar.platform.workshop.interfaces.rest.resources.MechanicResource;
import com.safecar.platform.workshop.interfaces.rest.resources.UpdateMechanicMetricsResource;
import com.safecar.platform.workshop.interfaces.rest.transform.MechanicResourceFromEntityAssembler;
import com.safecar.platform.workshop.interfaces.rest.transform.UpdateMechanicMetricsCommandFromResourceAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/api/v1/mechanic-profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Mechanics", description = "Mechanic management endpoints")
public class MechanicsController {

    private final MechanicCommandService commandService;

    public MechanicsController(MechanicCommandService commandService) {
        this.commandService = commandService;
    }

    @Operation(summary = "Update Mechanic Metrics by Profile ID")
    @PatchMapping("/{profileId}/metrics")
    public ResponseEntity<MechanicResource> updateMechanicMetrics(
            @PathVariable Long profileId,
            @RequestBody UpdateMechanicMetricsResource resource) {

        var command = UpdateMechanicMetricsCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command, profileId);

        if (result == null)
            return ResponseEntity.notFound().build();

        var mechanic = result.get();
        var mechanicResource = MechanicResourceFromEntityAssembler.toResourceFromEntity(mechanic);

        return ResponseEntity.ok(mechanicResource);
    }
}
