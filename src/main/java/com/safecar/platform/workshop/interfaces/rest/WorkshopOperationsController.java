package com.safecar.platform.workshop.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safecar.platform.workshop.domain.model.queries.GetAllWorkshopsQuery;
import com.safecar.platform.workshop.domain.model.queries.GetWorkshopOperationByWorkshopIdQuery;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;
import com.safecar.platform.workshop.domain.services.WorkshopOperationCommandService;
import com.safecar.platform.workshop.domain.services.WorkshopOperationQueryService;
import com.safecar.platform.workshop.interfaces.rest.resources.CreateWorkshopOperationResource;
import com.safecar.platform.workshop.interfaces.rest.resources.UpdateWorkshopMetricsResource;
import com.safecar.platform.workshop.interfaces.rest.resources.WorkshopOperationResource;
import com.safecar.platform.workshop.interfaces.rest.transform.WorkshopResourceFromEntityAssembler;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * REST controller for managing workshop operations.
 * <p>
 * RESTful controller following plural naming conventions for workshop metrics
 * and operations without service bay management.
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/workshop-operations", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Workshop Operations", description = "Workshop Operations Management")
public class WorkshopOperationsController {

    private final WorkshopOperationCommandService commandService;
    private final WorkshopOperationQueryService queryService;

    /**
     * Constructor for WorkshopOperationsController.
     * 
     * @param commandService the workshop operation command service
     * @param queryService   the workshop operation query service
     */
    public WorkshopOperationsController(WorkshopOperationCommandService commandService,
            WorkshopOperationQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    /**
     * Creates a new workshop operation with initial metrics.
     *
     * @param resource the workshop operation creation request
     * @return the created workshop operation information
     */
    @Operation(summary = "Create a new workshop operation", description = "Initializes a new workshop with metrics tracking.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Workshop operation created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or validation errors"),
            @ApiResponse(responseCode = "409", description = "Workshop operation already exists")
    })
    @PostMapping
    public ResponseEntity<WorkshopOperationResource> createWorkshopOperation(
            @Parameter(description = "Workshop operation creation request") @Valid @RequestBody CreateWorkshopOperationResource resource) {

        var workshop = new WorkshopId(resource.workshopId());
        var result = commandService.initializeWorkshopMetrics(workshop);

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        var workshopResource = WorkshopResourceFromEntityAssembler.toResourceFromEntity(result.get());
        return ResponseEntity.status(CREATED).body(workshopResource);
    }

    /**
     * Updates workshop metrics by performing specific actions.
     *
     * @param workshopId the unique identifier of the workshop
     * @param resource   the metrics update request
     * @return the updated workshop operation
     */
    @Operation(summary = "Update workshop metrics", description = "Updates workshop metrics by performing increment or completion actions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workshop metrics updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid action or input data"),
            @ApiResponse(responseCode = "404", description = "Workshop not found")
    })
    @PutMapping("/{workshopId}/metrics")
    public ResponseEntity<WorkshopOperationResource> updateWorkshopMetrics(
            @Parameter(description = "Unique identifier of the workshop", required = true, example = "1") @PathVariable Long workshopId,
            @Parameter(description = "Metrics update request") @Valid @RequestBody UpdateWorkshopMetricsResource resource) {

        var workshop = new WorkshopId(workshopId);

        var result = switch (resource.action()) {
            case "increment_appointments" -> commandService.incrementAppointmentMetrics(workshop);
            case "increment_serviceorders" -> commandService.incrementServiceOrderMetrics(workshop);
            case "complete_serviceorder" -> commandService.completeServiceOrder(workshop);
            default -> throw new IllegalArgumentException("Invalid action: " + resource.action());
        };

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var workshopResource = WorkshopResourceFromEntityAssembler.toResourceFromEntity(result.get());
        return ResponseEntity.ok(workshopResource);
    }

    /**
     * Retrieves a workshop by its ID with metrics.
     *
     * @param workshopId the unique identifier of the workshop
     * @return the workshop information with metrics
     */
    @Operation(summary = "Get workshop by ID", description = "Retrieves the complete workshop information with operational metrics.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workshop retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Workshop not found for the specified ID"),
            @ApiResponse(responseCode = "400", description = "Invalid workshop ID format")
    })
    @GetMapping("/{workshopId}")
    public ResponseEntity<WorkshopOperationResource> getWorkshopById(
            @Parameter(description = "Unique identifier of the workshop", required = true, example = "1") @PathVariable Long workshopId) {

        var workshop = new WorkshopId(workshopId);
        var query = new GetWorkshopOperationByWorkshopIdQuery(workshop);
        var operation = queryService.handle(query);

        if (operation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var workshopResource = WorkshopResourceFromEntityAssembler.toResourceFromEntity(operation.get());
        return ResponseEntity.ok(workshopResource);
    }

    /**
     * Retrieves all workshops with their metrics.
     *
     * @return list of workshops with their operational metrics
     */
    @Operation(summary = "Get all workshops", description = "Retrieves all workshops with their operational metrics.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workshops retrieved successfully (may be empty list)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<List<WorkshopOperationResource>> getAllWorkshops() {
        var query = new GetAllWorkshopsQuery();
        var workshops = queryService.handle(query);

        var resources = workshops.stream()
                .map(WorkshopResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resources);
    }
}
