package com.safecar.platform.devices.interfaces.rest.transform;

import com.safecar.platform.devices.domain.model.commands.UpdateDriverMetricsCommand;
import com.safecar.platform.devices.interfaces.rest.resources.UpdateDriverMetricsResource;

public class UpdateDriverMetricsCommandFromResourceAssembler {

    public static UpdateDriverMetricsCommand toCommandFromResource(UpdateDriverMetricsResource resource) {
        return new UpdateDriverMetricsCommand(
                resource.driverId());
    }
}
