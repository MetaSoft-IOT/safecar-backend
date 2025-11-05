package com.safecar.platform.workshop.domain.model.entities;

import com.safecar.platform.shared.domain.model.entities.AuditableModel;
import com.safecar.platform.workshop.domain.model.aggregates.WorkshopOperation;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "service_bays")
public class ServiceBay extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String label;

    @Embedded
    private WorkshopId workshop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workshop_operation_id")
    private WorkshopOperation workshopOperation;

    protected ServiceBay() {
    }

    public ServiceBay(String label, WorkshopId workshop, WorkshopOperation workshopOperation) {
        this.label = label;
        this.workshop = workshop;
        this.workshopOperation = workshopOperation;
    }
}
