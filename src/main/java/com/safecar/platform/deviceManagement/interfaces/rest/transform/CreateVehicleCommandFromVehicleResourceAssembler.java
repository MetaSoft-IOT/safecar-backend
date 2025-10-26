package com.safecar.platform.deviceManagement.interfaces.rest.transform;

import com.safecar.platform.deviceManagement.domain.model.commands.CreateVehicleCommand;
import com.safecar.platform.deviceManagement.interfaces.rest.resources.CreateVehicleResource;

public class CreateVehicleCommandFromVehicleResourceAssembler {
    public static CreateVehicleCommand toCommandFromVehicleResource(CreateVehicleResource resource) {
        return new CreateVehicleCommand(
                resource.driverId(),
                resource.licensePlate(),
                resource.brand(),
                resource.model()
        );
    }
}
