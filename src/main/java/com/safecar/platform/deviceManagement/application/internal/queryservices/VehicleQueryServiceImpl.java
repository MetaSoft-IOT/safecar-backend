package com.safecar.platform.deviceManagement.application.internal.queryservices;

import com.safecar.platform.deviceManagement.domain.model.aggregates.Vehicle;
import com.safecar.platform.deviceManagement.domain.model.queries.GetVehicleByDriverIdQuery;
import com.safecar.platform.deviceManagement.domain.model.queries.GetVehicleByIdQuery;
import com.safecar.platform.deviceManagement.domain.services.VehicleQueryService;
import com.safecar.platform.deviceManagement.infrastructure.persistence.jpa.repositories.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class VehicleQueryServiceImpl implements VehicleQueryService {
    public final VehicleRepository vehicleRepository;

    public VehicleQueryServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<Vehicle> handle(GetVehicleByDriverIdQuery query) {
        return vehicleRepository.findByDriverId(query.driverId());
    }

}
