package com.safecar.platform.deviceManagement.domain.services;

import com.safecar.platform.deviceManagement.domain.model.aggregates.Vehicle;
import com.safecar.platform.deviceManagement.domain.model.queries.GetVehicleByDriverIdQuery;
import com.safecar.platform.deviceManagement.domain.model.queries.GetVehicleByIdQuery;

import java.util.List;
import java.util.Optional;

public interface VehicleQueryService {

    List<Vehicle> handle(GetVehicleByDriverIdQuery query);

}
