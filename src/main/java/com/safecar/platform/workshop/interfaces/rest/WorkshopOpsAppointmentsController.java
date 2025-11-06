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
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;
import com.safecar.platform.workshop.domain.services.WorkshopAppointmentCommandService;
import com.safecar.platform.workshop.domain.services.WorkshopAppointmentQueryService;
import com.safecar.platform.workshop.interfaces.rest.resources.*;
import com.safecar.platform.workshop.interfaces.rest.transform.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing workshop appointments.
 */
@RestController
@RequestMapping(value = "/api/v1/workshops/{workshopId}/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Workshop Appointments", description = "Workshop appointment management endpoints")
public class WorkshopOpsAppointmentsController {

    /**
     * Command and Query services for workshop appointments.
     */
    private final WorkshopAppointmentCommandService commandService;
    private final WorkshopAppointmentQueryService queryService;

    /**
     * Create a new appointment for a specific workshop.
     */
    @PostMapping
    @Operation(summary = "Create a new appointment for a workshop")
    public ResponseEntity<AppointmentResource> createAppointment(
            @PathVariable Long workshopId,
            @Valid @RequestBody CreateAppointmentResource resource) {
        var command = CreateAppointmentCommandFromResourceAssembler.toCommandFromResource(resource);
        commandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Get an appointment by ID within a specific workshop.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get an appointment by ID within a workshop")
    public ResponseEntity<AppointmentResource> getAppointmentById(
            @PathVariable Long workshopId,
            @PathVariable Long id) {
        var query = new GetAppointmentByIdQuery(id);
        var appointment = queryService.handle(query);

        if (appointment.isEmpty())
            return ResponseEntity.notFound().build();

        var resource = AppointmentResourceFromAggregateAssembler.toResourceFromAggregate(appointment.get());
        return ResponseEntity.ok(resource);
    }

    /**
     * Get all appointments for a workshop with optional time range filter.
     */
    @GetMapping
    @Operation(summary = "Get all appointments for a workshop with optional time range filter")
    public ResponseEntity<List<AppointmentResource>> getAppointmentsByWorkshop(
            @PathVariable Long workshopId,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to) {
        // TODO: Create WorkshopId value object (using default display name for now)
        var workshop = new WorkshopId(workshopId,
                "Workshop " + workshopId);
        var query = new GetAppointmentsByWorkshopAndRangeQuery(workshop, from, to);
        var appointments = queryService.handle(query);

        var resources = appointments.stream()
                .map(AppointmentResourceFromAggregateAssembler::toResourceFromAggregate)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resources);
    }

    /**
     * Update an appointment (reschedule, cancel, or other modifications).
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Update an appointment (reschedule, cancel, or other modifications)")
    public ResponseEntity<AppointmentResource> updateAppointment(
            @PathVariable Long workshopId,
            @PathVariable Long id,
            @Valid @RequestBody RescheduleAppointmentResource resource) {

        var command = RescheduleAppointmentCommandFromResourceAssembler.toCommandFromResource(id, resource);
        commandService.handle(command);

        var query = new GetAppointmentByIdQuery(id);
        var appointment = queryService.handle(query);

        if (appointment.isEmpty())
            return ResponseEntity.notFound().build();

        var appointmentResource = AppointmentResourceFromAggregateAssembler.toResourceFromAggregate(appointment.get());
        return ResponseEntity.ok(appointmentResource);
    }



    /**
     * Associate an appointment with a work order.
     */
    @PutMapping("/{id}/work-order")
    @Operation(summary = "Associate an appointment with a work order")
    public ResponseEntity<AppointmentResource> setAppointmentWorkOrder(
            @PathVariable Long workshopId,
            @PathVariable Long id,
            @Valid @RequestBody LinkAppointmentToWorkOrderResource resource) {
        var command = LinkAppointmentToWorkOrderCommandFromResourceAssembler.toCommandFromResource(id, resource);
        commandService.handle(command);

        var query = new GetAppointmentByIdQuery(id);
        var appointment = queryService.handle(query);

        if (appointment.isEmpty())
            return ResponseEntity.notFound().build();

        var appointmentResource = AppointmentResourceFromAggregateAssembler.toResourceFromAggregate(appointment.get());
        return ResponseEntity.ok(appointmentResource);
    }

    /**
     * Add a note to an appointment.
     */
    @PostMapping("/{id}/notes")
    @Operation(summary = "Add a note to an appointment")
    public ResponseEntity<Void> addAppointmentNote(
            @PathVariable Long workshopId,
            @PathVariable Long id,
            @Valid @RequestBody AddAppointmentNoteResource resource) {
        var command = AddAppointmentNoteCommandFromResourceAssembler.toCommandFromResource(id, resource);
        commandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}