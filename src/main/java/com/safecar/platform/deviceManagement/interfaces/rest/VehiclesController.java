package com.safecar.platform.deviceManagement.interfaces.rest;

import com.safecar.platform.deviceManagement.domain.model.aggregates.Vehicle;
import com.safecar.platform.deviceManagement.domain.services.VehicleCommandService;
import com.safecar.platform.deviceManagement.domain.services.VehicleQueryService;
import com.safecar.platform.deviceManagement.interfaces.rest.resources.CreateVehicleResource;
import com.safecar.platform.deviceManagement.interfaces.rest.resources.VehicleResource;
import com.safecar.platform.deviceManagement.interfaces.rest.transform.CreateVehicleCommandFromVehicleResourceAssembler;
import com.safecar.platform.deviceManagement.interfaces.rest.transform.VehicleResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/vehicles",produces= APPLICATION_JSON_VALUE)
@Tag(name="Vehicles ",description = "Operations related to vehicles")
public class VehiclesController {
    private final VehicleCommandService vehicleCommandService;
    private final VehicleQueryService vehicleQueryService;

    public VehiclesController(VehicleCommandService vehicleCommandService, VehicleQueryService vehicleQueryService) {
        this.vehicleCommandService = vehicleCommandService;
        this.vehicleQueryService = vehicleQueryService;
    }

    @Operation(summary = "Create a new vehicle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "Vehicle created"),
            @ApiResponse(responseCode = "400", description = "Invalid Input"),
    })
    @PostMapping
    public ResponseEntity<VehicleResource> createVehicle(@RequestBody CreateVehicleResource resource){
        Optional<Vehicle> vehicle = this.vehicleCommandService
                .handle(CreateVehicleCommandFromVehicleResourceAssembler.toCommandFromVehicleResource(resource));
        return vehicle.map(source->new ResponseEntity<>(VehicleResourceFromEntityAssembler.toResourceFromEntity(source),CREATED))
                .orElseGet(()->ResponseEntity.badRequest().build());




}}
