package com.safecar.platform.deviceManagement.domain.model.aggregates;

import com.safecar.platform.deviceManagement.domain.model.commands.CreateVehicleCommand;
import com.safecar.platform.deviceManagement.domain.model.valueobjects.DriverId;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
@Entity
public class Vehicle extends AuditableAbstractAggregateRoot<Vehicle> {
  // Ensure this matches the repository method

    @Embedded
    private DriverId driverId;

    @NotNull
    private String licensePlate;

    @NotNull
    private String brand;

    @NotNull
    private String model;

    protected Vehicle() {}
    
    public Vehicle(CreateVehicleCommand command) {
        this.driverId = new DriverId(command.driverId());
        this.licensePlate = command.licensePlate();
        this.brand = command.brand();
        this.model = command.model();
    }

    public void updateVehicle(String licensePlate, String brand, String model) {
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
    }

    public Long getDriverId(){
        return this.driverId.driverId();
    }




}
