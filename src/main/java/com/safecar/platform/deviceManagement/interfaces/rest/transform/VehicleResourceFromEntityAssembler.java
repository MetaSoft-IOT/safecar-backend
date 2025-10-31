package com.safecar.platform.deviceManagement.interfaces.rest.transform;

import com.safecar.platform.deviceManagement.domain.model.aggregates.Vehicle;
import com.safecar.platform.deviceManagement.interfaces.rest.resources.VehicleResource;

public class VehicleResourceFromEntityAssembler {
    public static VehicleResource toResourceFromEntity(Vehicle vehicle) {
        return new VehicleResource(
                vehicle.getId(), // Convert UUID to Long
                vehicle.getDriverId(),
                vehicle.getLicensePlate(),
                vehicle.getBrand(),
                vehicle.getModel()
        );
    }
}
