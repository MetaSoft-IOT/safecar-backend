package com.safecar.platform.workshopOps.interfaces.rest;

import com.safecar.platform.workshopOps.domain.model.commands.*;
import com.safecar.platform.workshopOps.domain.model.queries.*;
import com.safecar.platform.workshopOps.domain.model.valueobjects.AppointmentStatus;
import com.safecar.platform.workshopOps.domain.services.AppointmentCommandService;
import com.safecar.platform.workshopOps.domain.services.AppointmentQueryService;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST Controller for managing appointments.
 * <p>
 * Provides endpoints for creating, retrieving, updating, and managing appointments.
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Appointment management endpoints")
public class AppointmentController {

    private final AppointmentCommandService appointmentCommandService;
    private final AppointmentQueryService appointmentQueryService;

    /**
     * Create a new appointment.
     *
     * @param resource the create appointment resource
     * @return the created appointment resource
     */
    @PostMapping
    @Operation(summary = "Create a new appointment")
    public ResponseEntity<AppointmentResource> createAppointment(@Valid @RequestBody CreateAppointmentResource resource) {
        var command = CreateAppointmentCommandFromResourceAssembler.toCommandFromResource(resource);
        var appointment = appointmentCommandService.handle(command);
        if (appointment.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var appointmentResource = AppointmentResourceFromEntityAssembler.toResourceFromEntity(appointment.get());
        return new ResponseEntity<>(appointmentResource, HttpStatus.CREATED);
    }

    /**
     * Get an appointment by ID.
     *
     * @param id the appointment ID
     * @return the appointment resource
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get an appointment by ID")
    public ResponseEntity<AppointmentResource> getAppointmentById(@PathVariable UUID id) {
        var query = new GetAppointmentByIdQuery(id);
        var appointment = appointmentQueryService.handle(query);
        if (appointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var appointmentResource = AppointmentResourceFromEntityAssembler.toResourceFromEntity(appointment.get());
        return ResponseEntity.ok(appointmentResource);
    }

    /**
     * Get an appointment by code.
     *
     * @param code the appointment code
     * @return the appointment resource
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "Get an appointment by code")
    public ResponseEntity<AppointmentResource> getAppointmentByCode(@PathVariable String code) {
        var query = new GetAppointmentByCodeQuery(code);
        var appointment = appointmentQueryService.handle(query);
        if (appointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var appointmentResource = AppointmentResourceFromEntityAssembler.toResourceFromEntity(appointment.get());
        return ResponseEntity.ok(appointmentResource);
    }

    /**
     * Get all appointments by customer ID.
     *
     * @param customerId the customer ID
     * @return list of appointment resources
     */
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get all appointments by customer ID")
    public ResponseEntity<List<AppointmentResource>> getAppointmentsByCustomerId(@PathVariable UUID customerId) {
        var query = new GetAllAppointmentsByCustomerIdQuery(customerId);
        var appointments = appointmentQueryService.handle(query);
        var appointmentResources = appointments.stream()
                .map(AppointmentResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointmentResources);
    }

    /**
     * Get all appointments by workshop ID.
     *
     * @param workshopId the workshop ID
     * @return list of appointment resources
     */
    @GetMapping("/workshop/{workshopId}")
    @Operation(summary = "Get all appointments by workshop ID")
    public ResponseEntity<List<AppointmentResource>> getAppointmentsByWorkshopId(@PathVariable UUID workshopId) {
        var query = new GetAllAppointmentsByWorkshopIdQuery(workshopId);
        var appointments = appointmentQueryService.handle(query);
        var appointmentResources = appointments.stream()
                .map(AppointmentResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointmentResources);
    }

    /**
     * Get all appointments by status.
     *
     * @param status the appointment status
     * @return list of appointment resources
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get all appointments by status")
    public ResponseEntity<List<AppointmentResource>> getAppointmentsByStatus(@PathVariable String status) {
        try {
            var appointmentStatus = AppointmentStatus.valueOf(status.toUpperCase());
            var query = new GetAllAppointmentsByStatusQuery(appointmentStatus);
            var appointments = appointmentQueryService.handle(query);
            var appointmentResources = appointments.stream()
                    .map(AppointmentResourceFromEntityAssembler::toResourceFromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(appointmentResources);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update appointment information.
     *
     * @param id the appointment ID
     * @param resource the update appointment information resource
     * @return the updated appointment resource
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update appointment information")
    public ResponseEntity<AppointmentResource> updateAppointmentInformation(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAppointmentInformationResource resource) {
        var command = UpdateAppointmentInformationCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var appointment = appointmentCommandService.handle(command);
        if (appointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var appointmentResource = AppointmentResourceFromEntityAssembler.toResourceFromEntity(appointment.get());
        return ResponseEntity.ok(appointmentResource);
    }

    /**
     * Confirm an appointment.
     *
     * @param id the appointment ID
     * @return the confirmed appointment resource
     */
    @PostMapping("/{id}/confirm")
    @Operation(summary = "Confirm an appointment")
    public ResponseEntity<AppointmentResource> confirmAppointment(@PathVariable UUID id) {
        var command = new ConfirmAppointmentCommand(id);
        var appointment = appointmentCommandService.handle(command);
        if (appointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var appointmentResource = AppointmentResourceFromEntityAssembler.toResourceFromEntity(appointment.get());
        return ResponseEntity.ok(appointmentResource);
    }

    /**
     * Start an appointment.
     *
     * @param id the appointment ID
     * @return the started appointment resource
     */
    @PostMapping("/{id}/start")
    @Operation(summary = "Start an appointment")
    public ResponseEntity<AppointmentResource> startAppointment(@PathVariable UUID id) {
        var command = new StartAppointmentCommand(id);
        var appointment = appointmentCommandService.handle(command);
        if (appointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var appointmentResource = AppointmentResourceFromEntityAssembler.toResourceFromEntity(appointment.get());
        return ResponseEntity.ok(appointmentResource);
    }

    /**
     * Complete an appointment.
     *
     * @param id the appointment ID
     * @return the completed appointment resource
     */
    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete an appointment")
    public ResponseEntity<AppointmentResource> completeAppointment(@PathVariable UUID id) {
        var command = new CompleteAppointmentCommand(id);
        var appointment = appointmentCommandService.handle(command);
        if (appointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var appointmentResource = AppointmentResourceFromEntityAssembler.toResourceFromEntity(appointment.get());
        return ResponseEntity.ok(appointmentResource);
    }

    /**
     * Cancel an appointment.
     *
     * @param id the appointment ID
     * @param resource the cancel appointment resource
     * @return the cancelled appointment resource
     */
    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel an appointment")
    public ResponseEntity<AppointmentResource> cancelAppointment(
            @PathVariable UUID id,
            @Valid @RequestBody CancelAppointmentResource resource) {
        var command = CancelAppointmentCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var appointment = appointmentCommandService.handle(command);
        if (appointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var appointmentResource = AppointmentResourceFromEntityAssembler.toResourceFromEntity(appointment.get());
        return ResponseEntity.ok(appointmentResource);
    }

    /**
     * Reschedule an appointment.
     *
     * @param id the appointment ID
     * @param resource the reschedule appointment resource
     * @return the rescheduled appointment resource
     */
    @PostMapping("/{id}/reschedule")
    @Operation(summary = "Reschedule an appointment")
    public ResponseEntity<AppointmentResource> rescheduleAppointment(
            @PathVariable UUID id,
            @Valid @RequestBody RescheduleAppointmentResource resource) {
        var command = RescheduleAppointmentCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var appointment = appointmentCommandService.handle(command);
        if (appointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var appointmentResource = AppointmentResourceFromEntityAssembler.toResourceFromEntity(appointment.get());
        return ResponseEntity.ok(appointmentResource);
    }

    /**
     * Assign a mechanic to an appointment.
     *
     * @param id the appointment ID
     * @param resource the assign mechanic resource
     * @return the updated appointment resource
     */
    @PostMapping("/{id}/assign-mechanic")
    @Operation(summary = "Assign a mechanic to an appointment")
    public ResponseEntity<AppointmentResource> assignMechanic(
            @PathVariable UUID id,
            @Valid @RequestBody AssignMechanicResource resource) {
        var command = AssignMechanicCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var appointment = appointmentCommandService.handle(command);
        if (appointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var appointmentResource = AppointmentResourceFromEntityAssembler.toResourceFromEntity(appointment.get());
        return ResponseEntity.ok(appointmentResource);
    }

    /**
     * Add a note to an appointment.
     *
     * @param id the appointment ID
     * @param resource the add appointment note resource
     * @return no content
     */
    @PostMapping("/{id}/notes")
    @Operation(summary = "Add a note to an appointment")
    public ResponseEntity<Void> addAppointmentNote(
            @PathVariable UUID id,
            @Valid @RequestBody AddAppointmentNoteResource resource) {
        var command = AddAppointmentNoteCommandFromResourceAssembler.toCommandFromResource(id, resource);
        appointmentCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
