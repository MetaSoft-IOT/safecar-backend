package com.safecar.platform.workshop.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safecar.platform.workshop.domain.model.queries.GetAppointmentByIdQuery;
import com.safecar.platform.workshop.domain.model.queries.GetAppointmentsByWorkshopAndRangeQuery;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;
import com.safecar.platform.workshop.domain.services.AppointmentCommandService;
import com.safecar.platform.workshop.domain.services.AppointmentQueryService;
import com.safecar.platform.workshop.interfaces.rest.resources.*;
import com.safecar.platform.workshop.interfaces.rest.transform.*;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/workshops/{workshopId}/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Workshop appointment management endpoints")
public class AppointmentsController {

    private final AppointmentCommandService commandService;
    private final AppointmentQueryService queryService;

    /**
     * Create Appointment for a specific workshop.
     */
    @PostMapping
    @Operation(summary = "Create a new appointment")
    public ResponseEntity<AppointmentResource> createAppointment(
            @PathVariable Long workshopId,
            @Valid @RequestBody CreateAppointmentResource resource) {

        var command = CreateAppointmentCommandFromResourceAssembler
                .toCommandFromResource(resource);
        var appointmentOpt = commandService.handle(command);

        if (appointmentOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var appointmentResource = AppointmentResourceFromAggregateAssembler
                .toResourceFromAggregate(appointmentOpt.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentResource);
    }

    /**
     * Get an appointment by its ID.
     */
    @GetMapping("/{appointmentId}")
    @Operation(summary = "Get appointment by ID")
    public ResponseEntity<AppointmentResource> getAppointmentById(
            @PathVariable Long workshopId,
            @PathVariable Long appointmentId) {
        var query = new GetAppointmentByIdQuery(appointmentId);
        var appointment = queryService.handle(query);

        if (appointment.isEmpty())
            return ResponseEntity.notFound().build();

        var resource = AppointmentResourceFromAggregateAssembler.toResourceFromAggregate(appointment.get());
        return ResponseEntity.ok(resource);
    }

    /**
     * Get all appointments for a workshop, optionally filtered by a time range.
     * 
     * @param workshopId the workshop ID
     * @param from       the start of the time range (optional)
     * @param to         the end of the time range (optional)
     * @return ResponseEntity with a list of appointment resources
     */
    @GetMapping
    @Operation(summary = "Get all appointments for a workshop with optional time range filter")
    public ResponseEntity<List<AppointmentResource>> getAppointmentsByWorkshop(
            @PathVariable Long workshopId,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to) {
        var workshop = new WorkshopId(workshopId);
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
    @PatchMapping("/{appointmentId}")
    @Operation(summary = "Update an appointment (reschedule, cancel, or other modifications)")
    public ResponseEntity<AppointmentResource> rescheduleAppointment(
            @PathVariable Long workshopId,
            @PathVariable Long appointmentId,
            @Valid @RequestBody RescheduleAppointmentResource resource) {

        var command = RescheduleAppointmentCommandFromResourceAssembler
                .toCommandFromResource(appointmentId, resource);
        commandService.handle(command);

        var query = new GetAppointmentByIdQuery(appointmentId);
        var appointment = queryService.handle(query);

        if (appointment.isEmpty())
            return ResponseEntity.notFound().build();

        var appointmentResource = AppointmentResourceFromAggregateAssembler
                .toResourceFromAggregate(appointment.get());
        return ResponseEntity.ok(appointmentResource);
    }

    /**
     * Update appointment status - unified endpoint for all status transitions.
     * Supports CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED status updates.
     */
    @PatchMapping("/{appointmentId}/status")
    @Operation(summary = "Update appointment status", description = "Updates appointment status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status value"),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "409", description = "Invalid status transition")
    })
    public ResponseEntity<AppointmentResource> updateAppointmentStatus(
            @PathVariable Long workshopId,
            @PathVariable Long appointmentId,
            @Valid @RequestBody UpdateAppointmentStatusResource resource) {

        var command = UpdateAppointmentStatusCommandFromResourceAssembler
                .toCommandFromResource(appointmentId, resource);
        var appointmentOpt = commandService.handle(command);

        if (!appointmentOpt.isPresent())
            return ResponseEntity.notFound().build();

        var appointmentResource = AppointmentResourceFromAggregateAssembler
                .toResourceFromAggregate(appointmentOpt.get());

        return ResponseEntity.ok(appointmentResource);
    }

    /**
     * Associate an appointment with a service order.
     */
    @PatchMapping("/{appointmentId}/service-order")
    @Operation(summary = "Associate appointment with service order")
    public ResponseEntity<AppointmentResource> setAppointmentServiceOrder(
            @PathVariable Long workshopId,
            @PathVariable Long appointmentId,
            @Valid @RequestBody LinkAppointmentToServiceOrderResource resource) {

        var command = LinkAppointmentToServiceOrderCommandFromResourceAssembler
                .toCommandFromResource(appointmentId, resource);
        commandService.handle(command);

        var query = new GetAppointmentByIdQuery(appointmentId);
        var appointment = queryService.handle(query);

        if (appointment.isEmpty())
            return ResponseEntity.notFound().build();

        var appointmentResource = AppointmentResourceFromAggregateAssembler
                .toResourceFromAggregate(appointment.get());

        return ResponseEntity.ok(appointmentResource);
    }

    /**
     * Add a note to an appointment.
     */
    @PostMapping("/{appointmentId}/notes")
    @Operation(summary = "Add note to appointment")
    public ResponseEntity<Void> addAppointmentNote(
            @PathVariable Long workshopId,
            @PathVariable Long appointmentId,
            @Valid @RequestBody AddAppointmentNoteResource resource) {

        var command = AddAppointmentNoteCommandFromResourceAssembler
                .toCommandFromResource(appointmentId, resource);
        commandService.handle(command);

        return ResponseEntity.noContent().build();
    }
}
