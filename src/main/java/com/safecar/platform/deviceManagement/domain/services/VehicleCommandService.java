package com.safecar.platform.deviceManagement.domain.services;

import com.safecar.platform.deviceManagement.domain.model.aggregates.Vehicle;
import com.safecar.platform.deviceManagement.domain.model.commands.CreateVehicleCommand;
import com.safecar.platform.deviceManagement.domain.model.commands.DeleteVehicleCommand;
import com.safecar.platform.deviceManagement.domain.model.commands.UpdateVehicleCommand;

import java.util.Optional;

public interface VehicleCommandService {

    Optional<Vehicle> handle(CreateVehicleCommand command);

    Optional<Vehicle> handle(UpdateVehicleCommand command);

    void handle(DeleteVehicleCommand command);
}
