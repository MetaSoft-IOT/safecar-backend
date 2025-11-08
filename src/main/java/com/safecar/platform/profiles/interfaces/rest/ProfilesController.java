package com.safecar.platform.profiles.interfaces.rest;

import com.safecar.platform.profiles.domain.services.PersonProfileQueryService;
import com.safecar.platform.profiles.domain.services.PersonProfileCommandService;
import com.safecar.platform.profiles.interfaces.rest.resource.PersonProfileResource;
import com.safecar.platform.profiles.domain.model.queries.GetPersonProfileByUserIdQuery;
import com.safecar.platform.profiles.interfaces.rest.resource.CreatePersonProfileResource;
import com.safecar.platform.profiles.interfaces.rest.transform.PersonProfileResourceFromEntityAssembler;
import com.safecar.platform.profiles.interfaces.rest.transform.CreatePersonProfileCommandFromResourceAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.MediaType;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * Profile Controller
 * <p>
 * This controller provides REST endpoints for managing user profiles,
 * including creation and retrieval of profiles.
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "Profile Management Endpoints")
public class ProfilesController {

    /**
     * Services
     */
    private final PersonProfileQueryService queryService;
    private final PersonProfileCommandService commandService;

    /**
     * Constructor
     */
    public ProfilesController(
            PersonProfileCommandService personProfileCommandService,
            PersonProfileQueryService personProfileQueryService) {
        this.commandService = personProfileCommandService;
        this.queryService = personProfileQueryService;
    }

    /**
     * Get a profile by user ID
     * 
     * @param userId The user ID
     * @return A {@link PersonProfileResource} resource for the profile, or a not
     *         found response if the profile could not be found.
     */
    @GetMapping
    @Operation(summary = "Get a profile by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile found"),
            @ApiResponse(responseCode = "404", description = "Profile not found") })
    public ResponseEntity<PersonProfileResource> getProfileByUserId(@RequestParam Long userId) {
        return _getProfileByUserId(userId);
    }

    /**
     * Create a new profile
     * <p>
     * Creates a basic person profile. Driver and Mechanic profiles are
     * automatically
     * created via events and can be updated through their respective BC endpoints.
     * </p>
     * 
     * @param resource The profile creation resource
     * @return A {@link PersonProfileResource} resource for the created profile
     */
    @PostMapping
    @Operation(summary = "Create a new person profile with automatic Driver/Mechanic creation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Person profile created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<PersonProfileResource> createNewPersonProfile(
            @RequestParam Long userId,
            @RequestBody CreatePersonProfileResource resource) {
        return _createNewPersonProfile(userId, resource);
    }

    private ResponseEntity<PersonProfileResource> _createNewPersonProfile(
            Long userId, CreatePersonProfileResource resource) {

        var command = CreatePersonProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var profile = commandService.handle(command, userId);

        var profileResource = PersonProfileResourceFromEntityAssembler.toResourceFromEntity(profile);
        return ResponseEntity.status(CREATED).body(profileResource);
    }

    private ResponseEntity<PersonProfileResource> _getProfileByUserId(Long userId) {
        var getUserProfileQuery = new GetPersonProfileByUserIdQuery(userId);

        var profile = queryService.handle(getUserProfileQuery);

        if (profile.isEmpty())
            return ResponseEntity.notFound().build();

        var profileEntity = profile.get();

        var profileResource = PersonProfileResourceFromEntityAssembler.toResourceFromEntity(profileEntity);
        return ResponseEntity.ok(profileResource);
    }
}
