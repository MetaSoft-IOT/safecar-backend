package com.safecar.platform.profiles.interfaces.rest;

import com.safecar.platform.profiles.domain.model.queries.GetDriverByUserIdQuery;
import com.safecar.platform.profiles.domain.model.queries.GetWorkshopMechanicByUserIdQuery;
import com.safecar.platform.profiles.domain.services.DriverCommandService;
import com.safecar.platform.profiles.domain.services.DriverQueryService;
import com.safecar.platform.profiles.domain.services.WorkshopMechanicCommandService;
import com.safecar.platform.profiles.domain.services.WorkshopMechanicQueryService;
import com.safecar.platform.profiles.interfaces.rest.resource.CreateDriverResource;
import com.safecar.platform.profiles.interfaces.rest.resource.CreateWorkshopMechanicResource;
import com.safecar.platform.profiles.interfaces.rest.resource.DriverResource;
import com.safecar.platform.profiles.interfaces.rest.resource.WorkshopMechanicResource;
import com.safecar.platform.profiles.interfaces.rest.transform.CreateDriverCommandFromResourceAssembler;
import com.safecar.platform.profiles.interfaces.rest.transform.CreateWorkshopMechanicCommandFromResourceAssembler;
import com.safecar.platform.profiles.interfaces.rest.transform.DriverResourceFromEntityAssembler;
import com.safecar.platform.profiles.interfaces.rest.transform.WorkshopMechanicResourceFromEntityAssembler;

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

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;



/**
 * REST controller for managing user profiles (Drivers and Workshop Mechanics).
 * 
 * This controller provides endpoints for retrieving and creating driver and workshop mechanic profiles
 * associated with user accounts in the SafeCar platform. It supports profile management
 * operations for both driver users and workshop mechanic administrators.
 */
@RestController
@RequestMapping(value = "/api/v1/profiles", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "User Profile Management Operations for Drivers and Workshop Mechanics")
public class ProfileController {

    private final DriverQueryService driverQueryService;
    private final WorkshopMechanicQueryService workshopMechanicQueryService;
    private final DriverCommandService driverCommandService;
    private final WorkshopMechanicCommandService workshopMechanicCommandService;

    /**
     * Constructor for ProfileController.
     * 
     * @param driverQueryService the service for driver profile queries
     * @param workshopMechanicQueryService the service for workshop mechanic profile queries  
     * @param driverCommandService the service for driver profile commands
     * @param workshopMechanicCommandService the service for workshop mechanic profile commands
     */
    public ProfileController(DriverQueryService driverQueryService,
                             WorkshopMechanicQueryService workshopMechanicQueryService,
                             DriverCommandService driverCommandService,
                             WorkshopMechanicCommandService workshopMechanicCommandService) {
        this.driverQueryService = driverQueryService;
        this.workshopMechanicQueryService = workshopMechanicQueryService;
        this.driverCommandService = driverCommandService;
        this.workshopMechanicCommandService = workshopMechanicCommandService;
    }

    /**
     * Retrieves driver profile information by user ID.
     *
     * @param userId the unique identifier of the user account
     * @return the driver profile associated with the user ID
     */
    @Operation(
        summary = "Get driver profile by user ID",
        description = "Retrieves the complete driver profile information including personal details, " +
                     "contact information, and identification data for the specified user ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Driver profile retrieved successfully",
            content = @Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = DriverResource.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Driver profile not found for the specified user ID",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid user ID format",
            content = @Content
        )
    })
    @GetMapping(value = "/driver/{userId}")
    public ResponseEntity<DriverResource> getDriver(
            @Parameter(
                description = "Unique identifier of the user account", 
                required = true,
                example = "12345"
            )
            @PathVariable Long userId) {
        
        var getDriverByUserIdQuery = new GetDriverByUserIdQuery(userId);
        var driver = driverQueryService.handle(getDriverByUserIdQuery);
        
        if (driver.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var driverResource = DriverResourceFromEntityAssembler
                .toResourceFromEntity(driver.get());

        return ResponseEntity.ok(driverResource);
    }

    /**
     * Retrieves workshop mechanic profile information by user ID.
     *
     * @param userId the unique identifier of the user account
     * @return the workshop mechanic profile associated with the user ID
     */
    @Operation(
        summary = "Get workshop mechanic profile by user ID",
        description = "Retrieves the complete workshop mechanic profile information including business details, " +
                     "contact information, company name, and identification data for the specified user ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Workshop mechanic profile retrieved successfully",
            content = @Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = WorkshopMechanicResource.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Workshop mechanic profile not found for the specified user ID",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid user ID format",
            content = @Content
        )
    })
    @GetMapping(value = "/workshop-mechanic/{userId}")
    public ResponseEntity<WorkshopMechanicResource> getWorkshopMechanic(
            @Parameter(
                description = "Unique identifier of the user account", 
                required = true,
                example = "67890"
            )
            @PathVariable Long userId) {
        
        var getWorkshopMechanicByUserIdQuery = new GetWorkshopMechanicByUserIdQuery(userId);
        var workshopMechanic = workshopMechanicQueryService.handle(getWorkshopMechanicByUserIdQuery);
        
        if (workshopMechanic.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var workshopMechanicResource = WorkshopMechanicResourceFromEntityAssembler
                .toResourceFromEntity(workshopMechanic.get());

        return ResponseEntity.ok(workshopMechanicResource);
    }

    /**
     * Creates a new driver profile.
     *
     * @param resource the driver profile creation request
     * @return the created driver profile information
     */
    @Operation(
        summary = "Create a new driver profile",
        description = "Creates a new driver profile with personal details, contact information, " +
                     "and identification data. The driver profile will be associated with a user account " +
                     "for authentication and access control."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Driver profile created successfully",
            content = @Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = DriverResource.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data or validation errors",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "Driver profile already exists for this DNI",
            content = @Content
        )
    })
    @PostMapping(value = "/driver/{userId}")
    public ResponseEntity<DriverResource> createDriver(
            @Parameter(
                description = "Unique identifier of the user account",
                required = true,
                example = "12345"
            )
            @PathVariable Long userId,
            @Parameter(
                description = "Driver profile creation request with all required information"
            )
            @Valid @RequestBody CreateDriverResource resource) {
        
        var createDriverCommand = CreateDriverCommandFromResourceAssembler
                .toCommandFromResource(resource);
        var driver = driverCommandService.handle(createDriverCommand, userId);
        
        if (driver.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var driverResource = DriverResourceFromEntityAssembler
                .toResourceFromEntity(driver.get());

        return ResponseEntity.status(CREATED).body(driverResource);
    }

    /**
     * Creates a new workshop mechanic profile.
     *
     * @param resource the workshop mechanic profile creation request
     * @return the created workshop mechanic profile information
     */
    @Operation(
        summary = "Create a new workshop mechanic profile",
        description = "Creates a new workshop mechanic profile with business details, contact information, " +
                     "company data, and identification. The workshop mechanic profile will be associated with " +
                     "a user account for authentication and service management."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Workshop mechanic profile created successfully", 
            content = @Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = WorkshopMechanicResource.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data or validation errors",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Workshop mechanic profile already exists for this tax ID",
            content = @Content
        )
    })
    @PostMapping(value = "/workshop-mechanic/{userId}")
    public ResponseEntity<WorkshopMechanicResource> createWorkshopMechanic(
            @Parameter(
                description = "Unique identifier of the user account",
                required = true,
                example = "67890"
            )
            @PathVariable Long userId,
            @Parameter(
                description = "Workshop mechanic profile creation request with all required information"
            )
            @Valid @RequestBody CreateWorkshopMechanicResource resource) {
        
        var createWorkshopMechanicCommand = CreateWorkshopMechanicCommandFromResourceAssembler
                .toCommandFromResource(resource);
        var workshopMechanic = workshopMechanicCommandService.handle(createWorkshopMechanicCommand, userId);
        
        if (workshopMechanic.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var workshopMechanicResource = WorkshopMechanicResourceFromEntityAssembler
                .toResourceFromEntity(workshopMechanic.get());

        return ResponseEntity.status(CREATED).body(workshopMechanicResource);
    }
}
