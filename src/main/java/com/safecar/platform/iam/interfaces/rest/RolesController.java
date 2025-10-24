package com.safecar.platform.iam.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safecar.platform.iam.domain.model.queries.GetAllRolesQuery;
import com.safecar.platform.iam.domain.services.RoleQueryService;
import com.safecar.platform.iam.interfaces.rest.resources.RoleResource;
import com.safecar.platform.iam.interfaces.rest.transform.RoleResourceFromEntityAssembler;

/**
 * Controller to handle role endpoints.
 * <p>
 * This class is used to handle role endpoints.
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Roles", description = "Available Role Endpoints")
public class RolesController {
    private final RoleQueryService roleQueryService;

    /**
     * Constructor.
     *
     * @param roleQueryService The role query service.
     */
    public RolesController(RoleQueryService roleQueryService) {
        this.roleQueryService = roleQueryService;
    }

    /**
     * Get all roles.
     *
     * @return The list of roles.
     */
    @GetMapping
    @Operation(summary = "Get all roles", description = "Get all roles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles retrieved successfully.") })
    public ResponseEntity<List<RoleResource>> getAllRoles() {
        var getAllRolesQuery = new GetAllRolesQuery();
        var roles = roleQueryService.handle(getAllRolesQuery);
        var roleResources = roles.stream().map(RoleResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(roleResources);
    }
}