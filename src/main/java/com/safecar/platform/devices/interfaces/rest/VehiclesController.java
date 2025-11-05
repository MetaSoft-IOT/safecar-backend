package com.safecar.platform.devices.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.safecar.platform.devices.domain.model.aggregates.Vehicle;
import com.safecar.platform.devices.domain.services.VehicleCommandService;
import com.safecar.platform.devices.domain.services.VehicleQueryService;
import com.safecar.platform.devices.interfaces.rest.resources.CreateVehicleResource;
import com.safecar.platform.devices.interfaces.rest.resources.VehicleResource;
import com.safecar.platform.devices.interfaces.rest.transform.CreateVehicleCommandFromVehicleResourceAssembler;
import com.safecar.platform.devices.interfaces.rest.transform.VehicleResourceFromEntityAssembler;

import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * REST controller for managing vehicles.
 * 
 * This controller provides endpoints for vehicle management including
 * creation, retrieval by ID, and retrieval by driver ID.
 */
@RestController
@RequestMapping(value = "/api/v1/vehicles", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Vehicles", description = "Vehicle Management Operations")
public class VehiclesController {
        /**
         * Command and query services for vehicle management.
         */
        private final VehicleCommandService vehicleCommandService;
        private final VehicleQueryService vehicleQueryService;

        /**
         * Constructor for VehiclesController.
         * @param vehicleCommandService the vehicle command service
         * @param vehicleQueryService the vehicle query service
         */
        public VehiclesController(VehicleCommandService vehicleCommandService,
                        VehicleQueryService vehicleQueryService) {
                this.vehicleCommandService = vehicleCommandService;
                this.vehicleQueryService = vehicleQueryService;
        }

        /**
         * Creates a new vehicle.
         *
         * @param resource the vehicle creation request
         * @return the created vehicle information
         */
        @Operation(
                summary = "Create a new vehicle",
                description = "Creates a new vehicle associated with a driver. The vehicle will be registered " +
                             "in the system with license plate, brand, and model information."
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "201", 
                        description = "Vehicle created successfully",
                        content = @Content(
                                mediaType = APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = VehicleResource.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "400", 
                        description = "Invalid input data or validation errors",
                        content = @Content
                ),
                @ApiResponse(
                        responseCode = "409",
                        description = "Vehicle with same license plate already exists",
                        content = @Content
                )
        })
        @PostMapping
        public ResponseEntity<VehicleResource> createVehicle(
                @Parameter(description = "Vehicle creation request with all required information")
                @Valid @RequestBody CreateVehicleResource resource) {
                
                Optional<Vehicle> vehicle = this.vehicleCommandService
                                .handle(CreateVehicleCommandFromVehicleResourceAssembler
                                                .toCommandFromVehicleResource(resource));
                return vehicle.map(source -> new ResponseEntity<>(
                                VehicleResourceFromEntityAssembler.toResourceFromEntity(source), CREATED))
                                .orElseGet(() -> ResponseEntity.badRequest().build());
        }

        /**
         * Retrieves a vehicle by its ID.
         *
         * @param vehicleId the unique identifier of the vehicle
         * @return the vehicle information
         */
        @Operation(
                summary = "Get vehicle by ID",
                description = "Retrieves the complete vehicle information including license plate, " +
                             "brand, model, and associated driver ID for the specified vehicle ID."
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Vehicle retrieved successfully",
                        content = @Content(
                                mediaType = APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = VehicleResource.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Vehicle not found for the specified ID",
                        content = @Content
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid vehicle ID format",
                        content = @Content
                )
        })
        @GetMapping("/{vehicleId}")
        public ResponseEntity<VehicleResource> getVehicleById(
                @Parameter(
                        description = "Unique identifier of the vehicle",
                        required = true,
                        example = "1"
                )
                @PathVariable Long vehicleId) {
                
                var getVehicleByIdQuery = new com.safecar.platform.devices.domain.model.queries.GetVehicleByIdQuery(vehicleId);
                var vehicle = vehicleQueryService.handle(getVehicleByIdQuery);
                
                if (vehicle.isEmpty()) {
                        return ResponseEntity.notFound().build();
                }
                
                var vehicleResource = VehicleResourceFromEntityAssembler
                                .toResourceFromEntity(vehicle.get());
                
                return ResponseEntity.ok(vehicleResource);
        }

        /**
         * Retrieves all vehicles associated with a specific driver.
         *
         * @param driverId the unique identifier of the driver
         * @return list of vehicles owned by the driver
         */
        @Operation(
                summary = "Get vehicles by driver ID", 
                description = "Retrieves all vehicles associated with a specific driver ID. " +
                             "Returns an empty list if the driver has no registered vehicles."
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Vehicles retrieved successfully (may be empty list)",
                        content = @Content(
                                mediaType = APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = VehicleResource.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid driver ID format",
                        content = @Content
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Driver not found",
                        content = @Content
                )
        })
        @GetMapping("/driver/{driverId}")
        public ResponseEntity<List<VehicleResource>> getVehiclesByDriverId(
                @Parameter(
                        description = "Unique identifier of the driver",
                        required = true,
                        example = "12345"
                )
                @PathVariable Long driverId) {
                
                var getVehicleByDriverIdQuery = new com.safecar.platform.devices.domain.model.queries.GetVehicleByDriverIdQuery(driverId);
                var vehicles = vehicleQueryService.handle(getVehicleByDriverIdQuery);
                
                var vehicleResources = vehicles.stream()
                                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
                                .toList();
                
                return ResponseEntity.ok(vehicleResources);
        }
}
