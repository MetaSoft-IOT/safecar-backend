package com.safecar.platform.deviceManagement.interfaces.rest.transform;

import com.safecar.platform.deviceManagement.domain.model.commands.DeleteVehicleCommand;

public class DeleteVehicleCommandFromResourceAssembler {
    public static DeleteVehicleCommand toCommandFromResource(DeleteVehicleCommand resource, Long  id) {
        return new DeleteVehicleCommand(
                id,
                resource.driverId()

        );
    }
}
