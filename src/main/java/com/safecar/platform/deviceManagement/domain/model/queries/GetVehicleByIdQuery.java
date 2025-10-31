package com.safecar.platform.deviceManagement.domain.model.queries;



public record GetVehicleByIdQuery(Long vehicleId) {


    public GetVehicleByIdQuery{
        if (vehicleId <= 0 ) {
            throw new IllegalArgumentException("Vehicle ID must be a positive non-null value.");
        }
    }


}
