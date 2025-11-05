package com.safecar.platform.workshop.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safecar.platform.workshop.domain.model.queries.GetServiceBaysByWorkshopQuery;
import com.safecar.platform.workshop.domain.model.queries.GetWorkshopByIdQuery;
import com.safecar.platform.workshop.domain.services.WorkshopOperationCommandService;
import com.safecar.platform.workshop.domain.services.WorkshopOperationQueryService;
import com.safecar.platform.workshop.interfaces.rest.resources.*;
import com.safecar.platform.workshop.interfaces.rest.transform.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/workshops", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Workshop Operations", description = "Workshop operation endpoints")
public class WorkshopOpsWorkshopsController {

    private final WorkshopOperationCommandService commandService;
    private final WorkshopOperationQueryService queryService;

    @PostMapping("/allocate-bay")
    @Operation(summary = "Allocate a service bay for a workshop")
    public ResponseEntity<Void> postAllocateServiceBay(@Valid @RequestBody AllocateServiceBayResource resource) {
        var command = AllocateServiceBayCommandFromResourceAssembler.toCommandFromResource(resource);
        commandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{workshopId}")
    @Operation(summary = "Get workshop by id")
    public ResponseEntity<Object> getById(@PathVariable Long workshopId) {
        var workshop = new com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId(workshopId, "Workshop " + workshopId);
        var query = new GetWorkshopByIdQuery(workshop);
        var op = queryService.handle(query);
        if (op.isEmpty()) return ResponseEntity.notFound().build();
        // For now return a simple map-like resource
        var opAggregate = op.get();
        var result = new com.safecar.platform.workshop.interfaces.rest.resources.WorkshopResource(
                opAggregate.getId(), opAggregate.getMechanicsCount(), opAggregate.getBaysCount());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{workshopId}/bays")
    @Operation(summary = "Get service bays for a workshop")
    public ResponseEntity<List<ServiceBayResource>> getServiceBaysByWorkshop(@PathVariable Long workshopId) {
        var workshop = new com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId(workshopId, "Workshop " + workshopId);
        var query = new GetServiceBaysByWorkshopQuery(workshop);
        var bays = queryService.handle(query);
        var resources = bays.stream().map(ServiceBayResourceFromEntityAssembler::toResourceFromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }
}
