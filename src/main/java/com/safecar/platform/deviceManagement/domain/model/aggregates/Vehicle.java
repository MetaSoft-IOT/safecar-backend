package com.safecar.platform.deviceManagement.domain.model.aggregates;

import com.safecar.platform.deviceManagement.domain.model.valueobjects.DriverId;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Entity
public class Vehicle extends AuditableAbstractAggregateRoot<Vehicle> {
    @Embedded
    private DriverId driverId;

    @NotNull
    private String licensePlate;

    @NotNull
    private String brand;

    @NotNull
    private String model;

    protected Vehicle() {}




}
