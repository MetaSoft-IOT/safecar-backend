package com.safecar.platform.deviceManagement.interfaces.rest.transform;

import com.safecar.platform.deviceManagement.domain.model.commands.UpdateVehicleCommand;

import java.util.UUID;

public class UpdateVehicleCommandFromResourceAssembler {
    public static UpdateVehicleCommand toCommandFromResource(UpdateVehicleCommand resource, Long id) {
        return new UpdateVehicleCommand(
                id,
                resource.driverId(),
                resource.licensePlate(),
                resource.brand(),
                resource.model()
        );
    }
}