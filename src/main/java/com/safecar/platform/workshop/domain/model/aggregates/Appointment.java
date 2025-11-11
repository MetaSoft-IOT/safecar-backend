package com.safecar.platform.workshop.domain.model.aggregates;

import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.safecar.platform.workshop.domain.model.commands.CreateAppointmentCommand;
import com.safecar.platform.workshop.domain.model.entities.AppointmentNote;
import com.safecar.platform.workshop.domain.model.events.*;
import com.safecar.platform.workshop.domain.model.valueobjects.*;

import jakarta.persistence.*;
import lombok.Getter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Workshop Appointment Aggregate - This aggregate Root represents an
 * appointment in the workshop operations context.
 */
@Getter
@Entity
@Table(name = "workshop_appointments")
public class Appointment extends AuditableAbstractAggregateRoot<Appointment> {

    /**
     * Appointment Status - This enum represents the various states an appointment
     * can be in.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentStatus status;

    /**
     * Scheduled Service Slot - The time slot for which the appointment is
     * scheduled.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startAt", column = @Column(name = "scheduled_start_at")),
            @AttributeOverride(name = "endAt", column = @Column(name = "scheduled_end_at"))
    })
    private ServiceSlot scheduledAt;

    /**
     * Service Order Reference - Reference to the service order that contains all
     * entity IDs.
     * This eliminates redundancy of workshop, vehicle, and driver IDs by using the
     * service order as single source.
     * The service order contains the complete context of the service (workshop +
     * vehicle + driver).
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_order_id")
    private ServiceOrder serviceOrder;

    /**
     * List of notes - The notes associated with the appointment.
     */
    @OneToMany(mappedBy = "workshopAppointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<AppointmentNote> notes;

    /**
     * Default constructor for JPA.
     */
    protected Appointment() {
        this.status = AppointmentStatus.PENDING;
        this.notes = new ArrayList<>();
    }

    /**
     * Constructs a WorkshopAppointment from a CreateAppointmentCommand and a
     * ServiceOrder.
     * 
     * @param command      The command containing appointment details
     * @param serviceOrder The service order that contains all related entity IDs
     */
    public Appointment(CreateAppointmentCommand command, ServiceOrder serviceOrder) {
        this();
        this.scheduledAt = command.slot();
        this.serviceOrder = serviceOrder;

        registerEvent(new AppointmentCreatedEvent(
                this.getId(),
                serviceOrder.getWorkshop(),
                serviceOrder.getVehicle(),
                serviceOrder.getDriver(),
                this.scheduledAt,
                serviceOrder.getId()));
    }

    /**
     * Link to Service Order - Links this appointment to a service order using the
     * service order code.
     * 
     * @param code The service order code to link to
     */
    public void linkToServiceOrder(ServiceOrderCode code) {
        if (code == null)
            throw new IllegalArgumentException("Service order code cannot be null");
        // This method kept for backward compatibility; it requires repository
        // resolution
        throw new UnsupportedOperationException(
                "linkToServiceOrder(code) is not supported; use service to resolve service order id and call updateServiceOrder(serviceOrderId, code)");
    }

    /**
     * Update Service Order - Changes the service order associated with this
     * appointment.
     * 
     * @param newServiceOrder the new service order to link to
     * @param code            service order code used for validation
     */
    public void updateServiceOrder(ServiceOrder newServiceOrder, ServiceOrderCode code) {
        if (code == null)
            throw new IllegalArgumentException("Service order code cannot be null");
        if (newServiceOrder == null)
            throw new IllegalArgumentException("Service order cannot be null");

        // Validation that workshops match
        if (!this.serviceOrder.getWorkshop().workshopId().equals(newServiceOrder.getWorkshop().workshopId()))
            throw new IllegalStateException("New service order must be from the same workshop");

        this.serviceOrder = newServiceOrder;
    }

    /**
     * Reschedules - This method reschedules the appointment to a new time slot.
     * 
     * @param slot The new service slot
     */
    public void reschedule(ServiceSlot slot) {
        if (slot == null)
            throw new IllegalArgumentException("Service slot cannot be null");

        if (AppointmentStatus.COMPLETED.equals(this.status) || AppointmentStatus.CANCELLED.equals(this.status))
            throw new IllegalStateException("Cannot reschedule completed or cancelled appointments");

        var oldSlot = this.scheduledAt;
        this.scheduledAt = slot;

        registerEvent(new AppointmentRescheduledEvent(this.getId(), oldSlot, slot));
    }

    /**
     * Cancels - This method cancels the appointment.
     */
    public void cancel() {
        if (AppointmentStatus.COMPLETED.equals(this.status))
            throw new IllegalStateException("Cannot cancel completed appointments");

        this.status = AppointmentStatus.CANCELLED;
        registerEvent(new AppointmentCanceledEvent(this.getId(), Instant.now()));
    }

    /**
     * Confirm - This method confirm the appointment.
     */
    public void confirm() {
        if (!AppointmentStatus.PENDING.equals(this.status))
            throw new IllegalStateException("Only pending appointments can be confirmed");
        this.status = AppointmentStatus.CONFIRMED;
    }

    /**
     * Start - This method starts the appointment service.
     */
    public void start() {
        if (!AppointmentStatus.CONFIRMED.equals(this.status))
            throw new IllegalStateException("Only confirmed appointments can be started");
        this.status = AppointmentStatus.IN_PROGRESS;
    }

    /**
     * Complete - This method complete the appointment.
     */
    public void complete() {
        if (!AppointmentStatus.IN_PROGRESS.equals(this.status))
            throw new IllegalStateException("Only in-progress appointments can be completed");
        this.status = AppointmentStatus.COMPLETED;
    }

    /**
     * Get Service Order ID - Gets the ID of the associated service order.
     * 
     * @return the service order ID
     */
    public Long getServiceOrderId() {
        return serviceOrder != null ? serviceOrder.getId() : null;
    }

    /**
     * Get Service Order - Gets the associated service order.
     * 
     * @return the service order
     */
    public ServiceOrder getServiceOrder() {
        return serviceOrder;
    }

    /**
     * Is Linked To Service Order - This method checks if the appointment is linked
     * to a service order.
     * 
     * @return true if linked to a service order, false otherwise
     */
    public boolean isLinkedToServiceOrder() {
        return serviceOrder != null;
    }

    // Convenience methods to access IDs from the associated ServiceOrder (avoiding
    // redundancy)

    /**
     * Get Workshop ID - Gets workshop ID from the associated service order
     * 
     * @return WorkshopId from service order, null if no service order linked
     */
    public WorkshopId getWorkshop() {
        return serviceOrder != null ? serviceOrder.getWorkshop() : null;
    }

    /**
     * Get Vehicle ID - Gets vehicle ID from the associated service order
     * 
     * @return VehicleId from service order, null if no service order linked
     */
    public VehicleId getVehicle() {
        return serviceOrder != null ? serviceOrder.getVehicle() : null;
    }

    /**
     * Get Driver ID - Gets driver ID from the associated service order
     * 
     * @return DriverId from service order, null if no service order linked
     */
    public DriverId getDriver() {
        return serviceOrder != null ? serviceOrder.getDriver() : null;
    }

    /**
     * Add Note - This method add notes to the current appointment.
     * 
     * @param content  The content of the note
     * @param authorId The ID of the author
     */
    public void addNote(String content, Long authorId) {
        if (content == null || content.trim().isEmpty())
            throw new IllegalArgumentException("Note content cannot be null or empty");
        if (authorId == null)
            throw new IllegalArgumentException("Author ID cannot be null");

        var note = new AppointmentNote(content, authorId, this);
        this.notes.add(note);
    }

    /**
     * Get Notes - Gets the list of notes for this appointment
     * 
     * @return List of appointment notes
     */
    public List<AppointmentNote> getNotes() {
        return new ArrayList<>(this.notes);
    }

    /**
     * Get WorkshopId as Long
     * 
     * @return Long value of the WorkshopId
     */
    public Long getWorkshopId() {
        return this.getWorkshop().workshopId();
    }

    public boolean updateAppointmentStatus(AppointmentStatus newAppointmentStatus) {
        if (this.status == newAppointmentStatus) {
            return false;
        }

        if (newAppointmentStatus == AppointmentStatus.PENDING) {
            return false;
        }

        if (newAppointmentStatus == AppointmentStatus.CONFIRMED) {
            this.confirm();
            return true;
        }

        if (newAppointmentStatus == AppointmentStatus.IN_PROGRESS) {
            this.start();
            return true;
        }

        if (newAppointmentStatus == AppointmentStatus.COMPLETED) {
            this.complete();
            return true;
        }

        if (newAppointmentStatus == AppointmentStatus.CANCELLED) {
            this.cancel();
            return true;
        }

        return false;
    }

}