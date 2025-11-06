package com.safecar.platform.workshop.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safecar.platform.workshop.domain.model.queries.*;
import com.safecar.platform.workshop.domain.services.WorkshopOrderCommandService;
import com.safecar.platform.workshop.domain.services.WorkshopOrderQueryService;
import com.safecar.platform.workshop.interfaces.rest.resources.*;
import com.safecar.platform.workshop.interfaces.rest.transform.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Workshop Ops Workshop Orders Controller - REST controller for managing workshop work orders.
 */
@RestController
@RequestMapping(value = "/api/v1/workshops/{workshopId}/work-orders", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Workshop Work Orders", description = "Work order management endpoints")
public class WorkshopOpsWorkshopOrdersController {

    /**
     * The command service for workshop orders.
     */
    private final WorkshopOrderCommandService commandService;
    private final WorkshopOrderQueryService queryService;

    /**
     * Create a new work order for a specific workshop.
     * @param workshopId The ID of the workshop.
     * @param resource The resource containing the work order details.
     * @return A ResponseEntity indicating the result of the operation.
     */
    @PostMapping
    @Operation(summary = "Create a new work order for a workshop")
    public ResponseEntity<Void> createWorkOrder(
            @PathVariable Long workshopId,
            @Valid @RequestBody CreateWorkOrderResource resource) {
        var command = CreateWorkOrderCommandFromResourceAssembler.toCommandFromResource(resource);
        commandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Get a work order by ID within a specific workshop.
     * @param workshopId The ID of the workshop.
     * @param id The ID of the work order.
     * @return A ResponseEntity containing the work order resource or not found.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a work order by ID within a workshop")
    public ResponseEntity<WorkOrderResource> getWorkOrderById(
            @PathVariable Long workshopId,
            @PathVariable Long id) {
        var query = new GetWorkOrderByIdQuery(id);
        var wo = queryService.handle(query);
        if (wo.isEmpty())
            return ResponseEntity.notFound().build();
        var resource = WorkOrderResourceFromAggregateAssembler.toResourceFromAggregate(wo.get());
        return ResponseEntity.ok(resource);
    }

    /**
     * Get all work orders for a workshop with optional status filter.
     * @param workshopId The ID of the workshop.
     * @param status The optional status of the work orders.
     * @return A ResponseEntity containing the list of work order resources.
     */
    @GetMapping
    @Operation(summary = "Get all work orders for a workshop with optional status filter")
    public ResponseEntity<List<WorkOrderResource>> getWorkOrdersByWorkshop(
            @PathVariable Long workshopId,
            @RequestParam(required = false) String status) {

        var workshop = new com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId(workshopId,
                "Workshop " + workshopId);
        com.safecar.platform.workshop.domain.model.valueobjects.WorkOrderStatus st = null;
        if (status != null)
            st = com.safecar.platform.workshop.domain.model.valueobjects.WorkOrderStatus.valueOf(status);

        var query = new GetWorkOrdersByWorkshopQuery(workshop, st);
        var list = queryService.handle(query);

        var resources = list.stream()
                .map(WorkOrderResourceFromAggregateAssembler::toResourceFromAggregate)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resources);
    }

    /**
     * Update a work order (e.g., close, modify status).
     * @param workshopId The ID of the workshop.
     * @param id The ID of the work order.
     * @return A ResponseEntity containing the work order resource or not found.
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Update a work order (e.g., close, modify status)")
    public ResponseEntity<WorkOrderResource> updateWorkOrder(
            @PathVariable Long workshopId,
            @PathVariable Long id) {
        var command = new com.safecar.platform.workshop.domain.model.commands.CloseWorkOrderCommand(id);
        commandService.handle(command);

        var query = new GetWorkOrderByIdQuery(id);
        var wo = queryService.handle(query);
        if (wo.isEmpty())
            return ResponseEntity.notFound().build();
        var resource = WorkOrderResourceFromAggregateAssembler.toResourceFromAggregate(wo.get());
        return ResponseEntity.ok(resource);
    }

    /**
     * Add appointment to work order.
     * @param workshopId The ID of the workshop.
     * @param id The ID of the work order.
     * @param resource The appointment resource to add.
     * @return A ResponseEntity indicating the result of the operation.
     */
    @PostMapping("/{id}/appointments")
    @Operation(summary = "Add appointment to work order")
    public ResponseEntity<Void> addAppointmentToWorkOrder(
            @PathVariable Long workshopId,
            @PathVariable Long id,
            @Valid @RequestBody com.safecar.platform.workshop.interfaces.rest.resources.CreateAppointmentResource resource) {
        var slot = new com.safecar.platform.workshop.domain.model.valueobjects.ServiceSlot(resource.startAt(),
                resource.endAt());
        var command = new com.safecar.platform.workshop.domain.model.commands.AddAppointmentToWorkOrderCommand(id,
                slot);
        commandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
