package com.safecar.platform.workshopOps.interfaces.acl;

import com.safecar.platform.workshopOps.domain.model.commands.CreateAppointmentCommand;
import com.safecar.platform.workshopOps.domain.model.queries.GetAllAppointmentsByCustomerIdQuery;
import com.safecar.platform.workshopOps.domain.model.queries.GetAllAppointmentsByWorkshopIdQuery;
import com.safecar.platform.workshopOps.domain.model.queries.GetAppointmentByIdQuery;
import com.safecar.platform.workshopOps.domain.services.AppointmentCommandService;
import com.safecar.platform.workshopOps.domain.services.AppointmentQueryService;
import com.safecar.platform.workshopOps.interfaces.acl.dto.AppointmentDto;
import com.safecar.platform.workshopOps.interfaces.acl.dto.CreateAppointmentDto;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Anti-Corruption Layer service for Appointments context.
 * <p>
 * Provides a facade for other bounded contexts to interact with appointments
 * without coupling to internal domain model details.
 * </p>
 */
@Service
public class AppointmentsContextFacade {

    private final AppointmentCommandService appointmentCommandService;
    private final AppointmentQueryService appointmentQueryService;

    public AppointmentsContextFacade(
            AppointmentCommandService appointmentCommandService,
            AppointmentQueryService appointmentQueryService) {
        this.appointmentCommandService = appointmentCommandService;
        this.appointmentQueryService = appointmentQueryService;
    }

    /**
     * Creates an appointment from another bounded context.
     *
     * @param code the appointment code
     * @param scheduledDate the scheduled date
     * @param serviceType the service type
     * @param description the description
     * @param customerId the customer ID
     * @param vehicleId the vehicle ID
     * @param workshopId the workshop ID
     * @return the appointment ID if created successfully, null otherwise
     */
    public UUID createAppointment(
            String code,
            LocalDateTime scheduledDate,
            String serviceType,
            String description,
            UUID customerId,
            UUID vehicleId,
            UUID workshopId) {
        var command = new CreateAppointmentCommand(
                code,
                scheduledDate,
                serviceType,
                description,
                customerId,
                vehicleId,
                workshopId
        );
        var appointment = appointmentCommandService.handle(command);
        return appointment.map(app -> app.getId()).orElse(null);
    }

    /**
     * Checks if an appointment exists by ID.
     *
     * @param appointmentId the appointment ID
     * @return true if the appointment exists, false otherwise
     */
    public boolean appointmentExists(UUID appointmentId) {
        var query = new GetAppointmentByIdQuery(appointmentId);
        return appointmentQueryService.handle(query).isPresent();
    }

    /**
     * Gets the total number of appointments for a customer.
     *
     * @param customerId the customer ID
     * @return the number of appointments
     */
    public int getAppointmentCountByCustomer(UUID customerId) {
        var query = new GetAllAppointmentsByCustomerIdQuery(customerId);
        return appointmentQueryService.handle(query).size();
    }

    /**
     * Gets the appointment status as a string.
     *
     * @param appointmentId the appointment ID
     * @return the status string, or null if appointment not found
     */
    public String getAppointmentStatus(UUID appointmentId) {
        var query = new GetAppointmentByIdQuery(appointmentId);
        var appointment = appointmentQueryService.handle(query);
        return appointment.map(app -> app.getStatus().name()).orElse(null);
    }

    /**
     * Creates an appointment using a DTO from another bounded context.
     *
     * @param dto the create appointment DTO
     * @return the appointment ID if created successfully, empty otherwise
     */
    public Optional<UUID> createAppointment(CreateAppointmentDto dto) {
        var command = new CreateAppointmentCommand(
                dto.code(),
                dto.scheduledDate(),
                dto.serviceType(),
                dto.description(),
                dto.customerId(),
                dto.vehicleId(),
                dto.workshopId()
        );
        var appointment = appointmentCommandService.handle(command);
        return appointment.map(app -> app.getId());
    }

    /**
     * Gets appointment information as a DTO.
     *
     * @param appointmentId the appointment ID
     * @return the appointment DTO if found, empty otherwise
     */
    public Optional<AppointmentDto> getAppointmentById(UUID appointmentId) {
        var query = new GetAppointmentByIdQuery(appointmentId);
        var appointment = appointmentQueryService.handle(query);
        return appointment.map(app -> new AppointmentDto(
                app.getId(),
                app.getCode(),
                app.getScheduledDate(),
                app.getStatus().name(),
                app.getCustomerId(),
                app.getVehicleId(),
                app.getWorkshopId()
        ));
    }

    /**
     * Gets all appointments for a customer as DTOs.
     *
     * @param customerId the customer ID
     * @return list of appointment DTOs
     */
    public List<AppointmentDto> getAppointmentsByCustomer(UUID customerId) {
        var query = new GetAllAppointmentsByCustomerIdQuery(customerId);
        var appointments = appointmentQueryService.handle(query);
        return appointments.stream()
                .map(app -> new AppointmentDto(
                        app.getId(),
                        app.getCode(),
                        app.getScheduledDate(),
                        app.getStatus().name(),
                        app.getCustomerId(),
                        app.getVehicleId(),
                        app.getWorkshopId()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Gets all appointments for a workshop as DTOs.
     *
     * @param workshopId the workshop ID
     * @return list of appointment DTOs
     */
    public List<AppointmentDto> getAppointmentsByWorkshop(UUID workshopId) {
        var query = new GetAllAppointmentsByWorkshopIdQuery(workshopId);
        var appointments = appointmentQueryService.handle(query);
        return appointments.stream()
                .map(app -> new AppointmentDto(
                        app.getId(),
                        app.getCode(),
                        app.getScheduledDate(),
                        app.getStatus().name(),
                        app.getCustomerId(),
                        app.getVehicleId(),
                        app.getWorkshopId()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Checks if a customer has any pending appointments.
     *
     * @param customerId the customer ID
     * @return true if customer has pending appointments, false otherwise
     */
    public boolean customerHasPendingAppointments(UUID customerId) {
        var query = new GetAllAppointmentsByCustomerIdQuery(customerId);
        var appointments = appointmentQueryService.handle(query);
        return appointments.stream()
                .anyMatch(app -> "PENDING".equals(app.getStatus().name())
                        || "CONFIRMED".equals(app.getStatus().name()));
    }

    /**
     * Gets the scheduled date of an appointment.
     *
     * @param appointmentId the appointment ID
     * @return the scheduled date, or null if appointment not found
     */
    public LocalDateTime getAppointmentScheduledDate(UUID appointmentId) {
        var query = new GetAppointmentByIdQuery(appointmentId);
        var appointment = appointmentQueryService.handle(query);
        return appointment.map(app -> app.getScheduledDate()).orElse(null);
    }
}

