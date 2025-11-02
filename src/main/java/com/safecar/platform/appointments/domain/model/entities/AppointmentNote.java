package com.safecar.platform.appointments.domain.model.entities;

import com.safecar.platform.appointments.domain.model.aggregates.Appointment;
import com.safecar.platform.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Getter
@Entity
@Table(name = "appointment_notes")
public class AppointmentNote extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private UUID authorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    protected AppointmentNote() {
    }

    public AppointmentNote(String content, UUID authorId, Appointment appointment) {
        this.content = content;
        this.authorId = authorId;
        this.appointment = appointment;
    }
}


