package com.safecar.platform.appointments.domain.model.aggregates;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.safecar.platform.appointments.domain.model.commands.CreateAppointmentCommand;
import com.safecar.platform.appointments.domain.model.entities.AppointmentNote;
import com.safecar.platform.appointments.domain.model.valueobjects.AppointmentStatus;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

@Getter
@Entity
@Table(name = "appointments")
public class Appointment extends AuditableAbstractAggregateRoot<Appointment> {

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false)
    private LocalDateTime scheduledDate;

    @Column
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentStatus status;

    @Column(nullable = false, length = 100)
    private String serviceType;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private UUID customerId;

    @Column(nullable = false)
    private UUID vehicleId;

    @Column
    private UUID mechanicId;

    @Column(nullable = false)
    private UUID workshopId;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<AppointmentNote> notes;

    public Appointment() {
        this.code = Strings.EMPTY;
        this.description = Strings.EMPTY;
        this.status = AppointmentStatus.PENDING;
        this.notes = new ArrayList<>();
    }

    public Appointment(CreateAppointmentCommand command) {
        this();
        this.code = command.code();
        this.scheduledDate = command.scheduledDate();
        this.serviceType = command.serviceType();
        this.description = command.description();
        this.customerId = command.customerId();
        this.vehicleId = command.vehicleId();
        this.workshopId = command.workshopId();
        validateScheduledDate();
    }

    /**
     * Confirma la cita.
     * @return La cita actualizada.
     */
    public Appointment confirm() {
        if (this.status != AppointmentStatus.PENDING) {
            throw new IllegalStateException("Solo se pueden confirmar citas pendientes");
        }
        this.status = AppointmentStatus.CONFIRMED;
        return this;
    }

    /**
     * Inicia el servicio de la cita.
     * @return La cita actualizada.
     */
    public Appointment start() {
        if (this.status != AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException("Solo se pueden iniciar citas confirmadas");
        }
        this.status = AppointmentStatus.IN_PROGRESS;
        return this;
    }

    /**
     * Completa la cita.
     * @return La cita actualizada.
     */
    public Appointment complete() {
        if (this.status != AppointmentStatus.IN_PROGRESS) {
            throw new IllegalStateException("Solo se pueden completar citas en progreso");
        }
        this.status = AppointmentStatus.COMPLETED;
        this.endDate = LocalDateTime.now();
        return this;
    }

    /**
     * Cancela la cita con un motivo.
     * @param reason El motivo de cancelación.
     * @return La cita actualizada.
     */
    public Appointment cancel(String reason) {
        if (this.status == AppointmentStatus.COMPLETED || this.status == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("No se puede cancelar una cita completada o ya cancelada");
        }
        this.status = AppointmentStatus.CANCELLED;
        addNote("Cancelación: " + reason, this.customerId);
        return this;
    }

    /**
     * Reprograma la cita.
     * @param newScheduledDate La nueva fecha programada.
     * @return La cita actualizada.
     */
    public Appointment reschedule(LocalDateTime newScheduledDate) {
        if (this.status == AppointmentStatus.COMPLETED || this.status == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("No se puede reprogramar una cita completada o cancelada");
        }
        if (newScheduledDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La nueva fecha debe ser futura");
        }
        this.scheduledDate = newScheduledDate;
        return this;
    }

    /**
     * Agrega una nota a la cita.
     * @param content El contenido de la nota.
     * @param authorId El ID del autor.
     */
    public void addNote(String content, UUID authorId) {
        AppointmentNote note = new AppointmentNote(content, authorId, this);
        this.notes.add(note);
    }

    /**
     * Asigna un mecánico a la cita.
     * @param mechanicId El ID del mecánico.
     * @return La cita actualizada.
     */
    public Appointment assignMechanic(UUID mechanicId) {
        if (mechanicId == null) {
            throw new IllegalArgumentException("El ID del mecánico no puede ser nulo");
        }
        this.mechanicId = mechanicId;
        return this;
    }

    /**
     * Actualiza la información de la cita.
     * @param serviceType El nuevo tipo de servicio.
     * @param description La nueva descripción.
     * @return La cita actualizada.
     */
    public Appointment updateInformation(String serviceType, String description) {
        this.serviceType = serviceType;
        this.description = description;
        return this;
    }

    private void validateScheduledDate() {
        if (this.scheduledDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha programada debe ser futura");
        }
    }
}
