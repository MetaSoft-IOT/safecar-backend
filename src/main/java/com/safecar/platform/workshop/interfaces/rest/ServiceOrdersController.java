package com.safecar.platform.workshop.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safecar.platform.workshop.domain.model.queries.GetServiceOrderByIdQuery;
import com.safecar.platform.workshop.domain.model.queries.GetServiceOrdersByWorkshopQuery;
import com.safecar.platform.workshop.domain.model.valueobjects.ServiceOrderStatus;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;
import com.safecar.platform.workshop.domain.services.ServiceOrderCommandService;
import com.safecar.platform.workshop.domain.services.ServiceOrderQueryService;
import com.safecar.platform.workshop.interfaces.rest.resources.CloseServiceOrderResource;
import com.safecar.platform.workshop.interfaces.rest.resources.CreateServiceOrderResource;
import com.safecar.platform.workshop.interfaces.rest.resources.ServiceOrderResource;
import com.safecar.platform.workshop.interfaces.rest.transform.CloseServiceOrderCommandFromResourceAssembler;
import com.safecar.platform.workshop.interfaces.rest.transform.CreateServiceOrderCommandFromResourceAssembler;
import com.safecar.platform.workshop.interfaces.rest.transform.ServiceOrderResourceFromAggregateAssembler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Workshop Service Orders Controller - REST controller for managing workshop
 * service orders.
 * <p>
 * In SafeCar's context, ServiceOrders represent the core operational units that
 * integrate:
 * - Workshop operations with IoT telemetry processing
 * - Driver-vehicle relationships for predictive maintenance insights
 * - Data-driven service delivery and automated workflow optimization
 * </p>
 * <p>
 * This controller supports SafeCar's SaaS model by enabling:
 * - Workshop-centric service order management
 * - Integration with vehicle IoT data for intelligent service recommendations
 * - Operational metrics collection for performance insights
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/workshops/{workshopId}/service-orders", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Service Orders", description = "Workshop service order management endpoints for SafeCar platform")
public class ServiceOrdersController {

    private final ServiceOrderCommandService commandService;
    private final ServiceOrderQueryService queryService;

    /**
     * Creates a new service order for a specific workshop.
     * <p>
     * In SafeCar context, this initiates a new service lifecycle that will
     * integrate
     * vehicle telemetry data, driver patterns, and workshop operational capacity.
     * </p>
     *
     * @param workshopId the workshop ID creating the service order
     * @param resource   the service order creation resource
     * @return ResponseEntity with created service order resource
     */
    @PostMapping
    @Operation(summary = "Create a new service order", description = "Creates a new service order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Service order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Workshop not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - active service order already exists for this vehicle")
    })
    public ResponseEntity<ServiceOrderResource> createServiceOrder(
            @Parameter @PathVariable Long workshopId,
            @Valid @RequestBody CreateServiceOrderResource resource) {

        var command = CreateServiceOrderCommandFromResourceAssembler.toCommandFromResource(resource);
        var serviceOrderOpt = commandService.handle(command);

        if (serviceOrderOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        var serviceOrderResource = ServiceOrderResourceFromAggregateAssembler
                .toResourceFromAggregate(serviceOrderOpt.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(serviceOrderResource);
    }

    /**
     * Retrieves a service order by ID within a specific workshop.
     *
     * @param workshopId     the workshop ID
     * @param serviceOrderId the service order ID
     * @return ResponseEntity with service order resource or NOT FOUND
     */
    @GetMapping("/{serviceOrderId}")
    @Operation(summary = "Get service order by ID", description = "Retrieves detailed service order information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service order found"),
            @ApiResponse(responseCode = "404", description = "Service order not found")
    })
    public ResponseEntity<ServiceOrderResource> getServiceOrderById(
            @Parameter @PathVariable Long workshopId,
            @Parameter @PathVariable Long serviceOrderId) {

        var query = new GetServiceOrderByIdQuery(serviceOrderId);
        var serviceOrder = queryService.handle(query);

        if (serviceOrder.isEmpty())
            return ResponseEntity.notFound().build();

        var resource = ServiceOrderResourceFromAggregateAssembler
                .toResourceFromAggregate(serviceOrder.get());

        return ResponseEntity.ok(resource);
    }

    /**
     * Retrieves all service orders for a workshop, optionally filtered by status.
     * <p>
     * Supports SafeCar's operational analytics by providing filtered views of
     * workshop service order pipeline for capacity planning and performance
     * monitoring.
     * </p>
     *
     * @param workshopId the workshop ID
     * @param status     optional status filter (OPEN, CLOSED)
     * @return ResponseEntity with list of service orders
     */
    @GetMapping
    @Operation(summary = "Get workshop service orders", description = "Retrieves service orders, optionally filtered by status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service orders retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Workshop not found")
    })
    public ResponseEntity<List<ServiceOrderResource>> getServiceOrdersByWorkshop(
            @Parameter @PathVariable Long workshopId,
            @Parameter @RequestParam(required = false) String status) {

        var query = new GetServiceOrdersByWorkshopQuery(new WorkshopId(workshopId), ServiceOrderStatus.valueOf(status.toUpperCase()));
        var serviceOrders = queryService.handle(query);

        var resources = serviceOrders.stream()
                .map(ServiceOrderResourceFromAggregateAssembler::toResourceFromAggregate)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resources);
    }

    /**
     * Closes a service order, marking it as completed.
     * <p>
     * In SafeCar's operational flow, this triggers:
     * - Finalization of telemetry data analysis for the service period
     * - Update of predictive maintenance recommendations
     * - Operational metrics computation for workshop performance insights
     * </p>
     *
     * @param workshopId     the workshop ID
     * @param serviceOrderId the service order ID to close
     * @param resource       optional closing details (reason, notes)
     * @return ResponseEntity with updated service order or error status
     */
    @PatchMapping("/{serviceOrderId}/close")
    @Operation(summary = "Close service order", description = "Closes the service order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service order closed successfully"),
            @ApiResponse(responseCode = "404", description = "Service order not found"),
            @ApiResponse(responseCode = "409", description = "Service order cannot be closed (has pending appointments)")
    })
    public ResponseEntity<ServiceOrderResource> closeServiceOrder(
            @Parameter @PathVariable Long workshopId,
            @Parameter @PathVariable Long serviceOrderId,
            @RequestBody CloseServiceOrderResource resource) {

        var command = CloseServiceOrderCommandFromResourceAssembler.toCommandFromResource(resource);
        var serviceOrderOpt = commandService.handle(command);

        if (serviceOrderOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        var serviceOrder = serviceOrderOpt.get();

        var responseResource = ServiceOrderResourceFromAggregateAssembler
                .toResourceFromAggregate(serviceOrder);
        return ResponseEntity.ok(responseResource);
    }
}
