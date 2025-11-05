package com.safecar.platform.devices.application.internal.commandservices;

import org.springframework.stereotype.Service;

import com.safecar.platform.devices.application.internal.outboundservices.acl.ExternalProfileService;
import com.safecar.platform.devices.domain.model.aggregates.Vehicle;
import com.safecar.platform.devices.domain.model.commands.CreateVehicleCommand;
import com.safecar.platform.devices.domain.model.commands.DeleteVehicleCommand;
import com.safecar.platform.devices.domain.model.commands.UpdateVehicleCommand;
import com.safecar.platform.devices.domain.services.VehicleCommandService;
import com.safecar.platform.devices.infrastructure.persistence.jpa.repositories.VehicleRepository;

import java.util.Optional;

/**
 * Vehicle Command Service Implementation
 * <p>
 * This service handles commands related to vehicle management, including
 * creation, updating, and deletion of vehicles. It integrates with the
 * Profiles bounded context through ACL to validate driver existence.
 * </p>
 */
@Service
public class VehicleCommandServiceImpl implements VehicleCommandService {
    
    private final VehicleRepository vehicleRepository;
    private final ExternalProfileService externalProfileService;

    /**
     * Constructor for VehicleCommandServiceImpl
     * 
     * @param vehicleRepository Vehicle Repository
     * @param externalProfileService External Profile Service (ACL)
     */
    public VehicleCommandServiceImpl(VehicleRepository vehicleRepository, 
                                   ExternalProfileService externalProfileService) {
        this.vehicleRepository = vehicleRepository;
        this.externalProfileService = externalProfileService;
    }

    // {@inheritDoc}
    @Override
    public Optional<Vehicle> handle(CreateVehicleCommand command) {
        // Validate that the driver exists before creating the vehicle
        externalProfileService.validateDriverExists(command.driverId());
        
        // Check if a vehicle with the same license plate already exists
        if (vehicleRepository.existsByLicensePlate(command.licensePlate())) {
            throw new IllegalArgumentException("Vehicle with license plate '" + command.licensePlate() + "' already exists");
        }
        
        var vehicle = new Vehicle(command);
        var vehicleCreated = vehicleRepository.save(vehicle);
        return Optional.of(vehicleCreated);
    }

    // {@inheritDoc}
    @Override
    public Optional<Vehicle> handle(UpdateVehicleCommand command) {

        var vehicleId = command.vehicleId();
        var vehicle = this.vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with ID: " + command.vehicleId()));
        vehicle.updateVehicle(command.licensePlate(), command.brand(), command.model());
        var vehicleUpdated = vehicleRepository.save(vehicle);
        return Optional.of(vehicleUpdated);
    }

    // {@inheritDoc}
    @Override
    public void handle(DeleteVehicleCommand command) {

        var exists = this.vehicleRepository.existsById(command.vehicleId());
        if (!exists)
            throw new IllegalArgumentException("Vehicle not found with ID: " + command.vehicleId());
        this.vehicleRepository.deleteById(command.vehicleId());
    }
}