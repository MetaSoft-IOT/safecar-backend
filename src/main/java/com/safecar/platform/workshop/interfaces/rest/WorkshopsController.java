package com.safecar.platform.workshop.interfaces.rest;

import com.safecar.platform.workshop.domain.model.queries.GetWorkshopByIdQuery;
import com.safecar.platform.workshop.domain.services.WorkshopCommandService;
import com.safecar.platform.workshop.domain.services.WorkshopQueryService;
import com.safecar.platform.workshop.interfaces.rest.resources.CreateWorkshopResource;
import com.safecar.platform.workshop.interfaces.rest.resources.WorkshopResource;
import com.safecar.platform.workshop.interfaces.rest.transform.CreateWorkshopCommandFromResourceAssembler;
import com.safecar.platform.workshop.interfaces.rest.transform.WorkshopEntityResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Workshop Entity Controller
 * Handles Workshop aggregate operations separate from WorkshopOperation
 * operations
 * Route: /api/v1/workshop (Workshop entity management)
 * Note: /api/v1/workshop-operations is used by WorkshopOperationsController for
 * operations/metrics
 */
@RestController
@RequestMapping(value = "/api/v1/workshop", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Workshops", description = "Workshop entity management endpoints")
public class WorkshopsController {

    private final WorkshopCommandService workshopCommandService;
    private final WorkshopQueryService workshopQueryService;

    public WorkshopsController(WorkshopCommandService workshopCommandService,
            WorkshopQueryService workshopQueryService) {
        this.workshopCommandService = workshopCommandService;
        this.workshopQueryService = workshopQueryService;
    }

    /**
     * Creates a new workshop.
     *
     * @param createWorkshopResource the {@link CreateWorkshopResource} resource
     * @return the {@link WorkshopResource} resource
     */
    @Operation(summary = "Create a new workshop")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Workshop created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "409", description = "Workshop already exists for business profile")
    })
    @PostMapping
    public ResponseEntity<WorkshopResource> createWorkshop(
            @RequestBody CreateWorkshopResource createWorkshopResource) {

        var createWorkshopCommand = CreateWorkshopCommandFromResourceAssembler
                .toCommandFromResource(createWorkshopResource);
        var workshop = workshopCommandService.handle(createWorkshopCommand);

        if (workshop.isEmpty())
            return ResponseEntity.badRequest().build();

        var workshopResource = WorkshopEntityResourceFromEntityAssembler
                .toResourceFromEntity(workshop.get());
        return new ResponseEntity<>(workshopResource, HttpStatus.CREATED);
    }

    /**
     * Gets a workshop by its ID.
     *
     * @param workshopId the workshop ID
     * @return the {@link WorkshopResource} resource
     */
    @Operation(summary = "Get workshop by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workshop found"),
            @ApiResponse(responseCode = "404", description = "Workshop not found")
    })
    @GetMapping("/{workshopId}")
    public ResponseEntity<WorkshopResource> getWorkshopById(
            @Parameter(required = true) @PathVariable Long workshopId) {

        var getWorkshopByIdQuery = new GetWorkshopByIdQuery(workshopId);
        var workshop = workshopQueryService.handle(getWorkshopByIdQuery);

        if (workshop.isEmpty())
            return ResponseEntity.notFound().build();

        var workshopResource = WorkshopEntityResourceFromEntityAssembler
                .toResourceFromEntity(workshop.get());
        return ResponseEntity.ok(workshopResource);
    }
}