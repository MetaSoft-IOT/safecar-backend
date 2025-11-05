package com.safecar.platform.devices.application.internal.queryservices;

import org.springframework.stereotype.Service;

import com.safecar.platform.devices.domain.model.aggregates.Vehicle;
import com.safecar.platform.devices.domain.model.queries.GetVehicleByDriverIdQuery;
import com.safecar.platform.devices.domain.model.queries.GetVehicleByIdQuery;
import com.safecar.platform.devices.domain.services.VehicleQueryService;
import com.safecar.platform.devices.infrastructure.persistence.jpa.repositories.VehicleRepository;

import java.util.List;
import java.util.Optional;

/**
 * Vehicle Query Service Implementation
 * <p>
 * This class implements the VehicleQueryService interface to handle queries
 * related to Vehicle entities.
 * </p>
 */
@Service
public class VehicleQueryServiceImpl implements VehicleQueryService {
    /**
     * The Vehicle Repository
     */
    public final VehicleRepository vehicleRepository;

    /**
     * Constructor for VehicleQueryServiceImpl
     * 
     * @param vehicleRepository The Vehicle Repository
     */
    public VehicleQueryServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    // {@inheritDoc}
    @Override
    public List<Vehicle> handle(GetVehicleByDriverIdQuery query) {
        return vehicleRepository.findByDriverId_DriverId(query.driverId());
    }

    // {@inheritDoc}
    @Override
    public Optional<Vehicle> handle(GetVehicleByIdQuery query) {
        return vehicleRepository.findById(query.vehicleId());
    }
}
