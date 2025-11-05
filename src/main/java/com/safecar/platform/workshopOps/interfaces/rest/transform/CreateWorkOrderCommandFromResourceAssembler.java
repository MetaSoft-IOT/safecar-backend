package com.safecar.platform.workshopOps.interfaces.rest.transform;

import com.safecar.platform.workshopOps.domain.model.commands.OpenWorkOrderCommand;
import com.safecar.platform.workshopOps.domain.model.valueobjects.*;
import com.safecar.platform.workshopOps.interfaces.rest.resources.CreateWorkOrderResource;

import java.time.Instant;

/**
 * Create Work Order Command From Resource Assembler - Assembler to convert
 * CreateWorkOrderResource into OpenWorkOrderCommand
 */
public class CreateWorkOrderCommandFromResourceAssembler {
    /**
     * Convert CreateWorkOrderResource to OpenWorkOrderCommand
     *
     * @param resource CreateWorkOrderResource
     * @return OpenWorkOrderCommand
     */
    public static OpenWorkOrderCommand toCommandFromResource(CreateWorkOrderResource resource) {
        var workshopId = new WorkshopId(resource.workshopId(), "Workshop " + resource.workshopId());
        var vehicleId = new VehicleId(resource.vehicleId(), "PLATE-" + resource.vehicleId());
        var driverId = new DriverId(resource.driverId(), "Driver " + resource.driverId());
        var code = new WorkOrderCode(resource.code(), resource.workshopId());
        var openedAt = resource.openedAt() == null ? Instant.now() : resource.openedAt();

        return new OpenWorkOrderCommand(workshopId, vehicleId, driverId, code, openedAt);
    }
}
