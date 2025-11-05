package com.safecar.platform.workshopOps.interfaces.rest;

import com.safecar.platform.workshopOps.domain.model.queries.*;
import com.safecar.platform.workshopOps.domain.model.valueobjects.WorkshopId;
import com.safecar.platform.workshopOps.domain.services.WorkshopAppointmentCommandService;
import com.safecar.platform.workshopOps.domain.services.WorkshopAppointmentQueryService;
import com.safecar.platform.workshopOps.interfaces.rest.resources.*;
import com.safecar.platform.workshopOps.interfaces.rest.transform.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing workshop appointments.
 */
@RestController
@RequestMapping(value = "/api/v1/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Workshop Appointments", description = "Workshop appointment management endpoints")
public class WorkshopOpsAppointmentsController {

    /**
     * Command and Query services for workshop appointments.
     */
    private final WorkshopAppointmentCommandService commandService;
    private final WorkshopAppointmentQueryService queryService;

    /**
     * Create a new appointment.
     */
    @PostMapping
    @Operation(summary = "Create a new appointment")
    public ResponseEntity<AppointmentResource> postCreate(@Valid @RequestBody CreateAppointmentResource resource) {
        var command = CreateAppointmentCommandFromResourceAssembler.toCommandFromResource(resource);
        commandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Get an appointment by ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get an appointment by ID")
    public ResponseEntity<AppointmentResource> getById(@PathVariable Long id) {
        var query = new GetAppointmentByIdQuery(id);
        var appointment = queryService.handle(query);

        if (appointment.isEmpty())
            return ResponseEntity.notFound().build();

        var resource = AppointmentResourceFromAggregateAssembler.toResourceFromAggregate(appointment.get());
        return ResponseEntity.ok(resource);
    }

    /**
     * Get appointments by workshop and time range.
     */
    @GetMapping("/workshop/{workshopId}")
    @Operation(summary = "Get appointments by workshop and time range")
    public ResponseEntity<List<AppointmentResource>> getByWorkshopAndRange(
            @PathVariable Long workshopId,
            @RequestParam Instant from,
            @RequestParam Instant to) {
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
     * Reschedule an appointment.
     */
    @PatchMapping("/{id}/reschedule")
    @Operation(summary = "Reschedule an appointment")
    public ResponseEntity<AppointmentResource> patchReschedule(
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
     * Cancel an appointment.
     */
    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel an appointment")
    public ResponseEntity<AppointmentResource> patchCancel(
            @PathVariable Long id,
            @Valid @RequestBody CancelAppointmentResource resource) {

        var command = CancelAppointmentCommandFromResourceAssembler.toCommandFromResource(id, resource);
        commandService.handle(command);

        var query = new GetAppointmentByIdQuery(id);
        var appointment = queryService.handle(query);

        if (appointment.isEmpty())
            return ResponseEntity.notFound().build();

        var appointmentResource = AppointmentResourceFromAggregateAssembler.toResourceFromAggregate(appointment.get());
        return ResponseEntity.ok(appointmentResource);
    }

    /**
     * Link an appointment to a work order.
     */
    @PatchMapping("/{id}/link-to-work-order")
    @Operation(summary = "Link an appointment to a work order")
    public ResponseEntity<AppointmentResource> patchLinkToWorkOrder(
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
    public ResponseEntity<Void> postAddNote(
            @PathVariable Long id,
            @Valid @RequestBody AddAppointmentNoteResource resource) {
        var command = AddAppointmentNoteCommandFromResourceAssembler.toCommandFromResource(id, resource);
        commandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}