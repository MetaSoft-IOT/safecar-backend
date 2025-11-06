package com.safecar.platform.profiles.interfaces.rest;

import com.safecar.platform.profiles.domain.services.PersonProfileCommandService;
import com.safecar.platform.profiles.domain.services.PersonProfileQueryService;
import com.safecar.platform.profiles.interfaces.rest.resource.CreatePersonProfileResource;
import com.safecar.platform.profiles.interfaces.rest.resource.PersonProfileResource;
import com.safecar.platform.profiles.interfaces.rest.transform.CreatePersonProfileCommandFromResourceAssembler;
import com.safecar.platform.profiles.interfaces.rest.transform.PersonProfileResourceFromEntityAssembler;

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
@RequestMapping(value = "/api/v1/users", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "User Profile Management Operations for Drivers and Workshop Mechanics")
public class ProfileController {

    private final PersonProfileQueryService personProfileQueryService;
    private final PersonProfileCommandService personProfileCommandService;

    /**
     * Constructor for ProfileController.
     * 
     * @param driverQueryService the service for driver profile queries
     * @param workshopMechanicQueryService the service for workshop mechanic profile queries  
     * @param driverCommandService the service for driver profile commands
     * @param workshopMechanicCommandService the service for workshop mechanic profile commands
     */
    public ProfileController(PersonProfileQueryService personProfileQueryService,
                             PersonProfileCommandService personProfileCommandService) {
        this.personProfileQueryService = personProfileQueryService;
        this.personProfileCommandService = personProfileCommandService;
    }

    /**
     * Retrieves person profile information by user ID.
     *
     * @param userId the unique identifier of the user account
     * @return the person profile associated with the user ID
     */
    @Operation(
        summary = "Get person profile by user ID",
        description = "Retrieves the complete person profile information including personal details, " +
                     "contact information, and identification data for the specified user ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Person profile retrieved successfully",
            content = @Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = PersonProfileResource.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Person profile not found for the specified user ID",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid user ID format",
            content = @Content
        )
    })
    @GetMapping(value = "/{userId}/profiles")
    public ResponseEntity<PersonProfileResource> getPersonProfile(
            @Parameter(
                description = "Unique identifier of the user account", 
                required = true,
                example = "12345"
            )
            @PathVariable Long userId) {
        
        var person = personProfileQueryService.findByUserId(userId);
        if (person.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var resource = PersonProfileResourceFromEntityAssembler.toResourceFromEntity(person.get());
        return ResponseEntity.ok(resource);
    }



    /**
     * Creates a new person profile.
     *
     * @param resource the person profile creation request
     * @return the created person profile information
     */
    @Operation(
        summary = "Create a new person profile",
        description = "Creates a new person profile with personal details"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Person profile created successfully",
            content = @Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = PersonProfileResource.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data or validation errors",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "Person profile already exists for this DNI",
            content = @Content
        )
    })
    @PostMapping(value = "/{userId}")
    public ResponseEntity<PersonProfileResource> createPersonProfile(
            @Parameter(
                required = true
            )
            @RequestParam Long userId,
            @Parameter(
                description = "Person profile creation request with all required information"
            )
            @Valid @RequestBody CreatePersonProfileResource resource) {
        
        var command = CreatePersonProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var created = personProfileCommandService.handle(command, userId);
        if (created.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var resourceOut = PersonProfileResourceFromEntityAssembler.toResourceFromEntity(created.get());
        return ResponseEntity.status(CREATED).body(resourceOut);
    }
}
