package com.safecar.platform.deviceManagement.application.internal.commandservices;

import com.safecar.platform.deviceManagement.domain.model.aggregates.Vehicle;
import com.safecar.platform.deviceManagement.domain.model.commands.CreateVehicleCommand;
import com.safecar.platform.deviceManagement.domain.model.commands.UpdateVehicleCommand;
import com.safecar.platform.deviceManagement.domain.services.VehicleCommandService;
import com.safecar.platform.deviceManagement.infrastructure.persistence.jpa.repositories.VehicleRepository;

import java.util.Optional;

public class VehicleCommandServiceImpl implements VehicleCommandService {
    private final VehicleRepository vehicleRepository;

    public VehicleCommandServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Optional<Vehicle> handle(CreateVehicleCommand command) {
        var vehicle = new Vehicle(command);
        var vehicleCreated = vehicleRepository.save(vehicle);
        return Optional.of(vehicleCreated);
    }

    @Override
    public Optional<Vehicle> handle(UpdateVehicleCommand command) {
        var vehicle = this.vehicleRepository.findById(command.vehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with ID: " + command.vehicleId()));
        vehicle.updateVehicle(command .licensePlate(), command.brand(), command.model());
        var vehicleUpdated = vehicleRepository.save(vehicle);
        return Optional.of(vehicleUpdated);

    }

    @Override
    public void handleDelete(Long vehicleId) {
        var vehicle = this.vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with ID: " + vehicleId));
        this.vehicleRepository.delete(vehicle);
    }
}