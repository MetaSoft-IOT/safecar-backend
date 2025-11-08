package com.safecar.platform.devices.application.internal.commandservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Vehicle Command Service Implementation with ACL Integration
 * <p>
 * This service handles commands related to vehicle management, including
 * creation, updating, and deletion of vehicles. It integrates with the
 * Profiles bounded context through ACL for driver validation.
 * </p>
 */
@Service
public class VehicleCommandServiceImpl implements VehicleCommandService {
    
    private static final Logger logger = LoggerFactory.getLogger(VehicleCommandServiceImpl.class);
    
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
        logger.info("Processing vehicle creation for driver: {} with license plate: {}", 
                   command.driverId(), command.licensePlate());

        try {
            // Validate that the driver profile exists before creating the vehicle
            if (!externalProfileService.validatePersonProfileExists(command.driverId())) {
                throw new IllegalArgumentException("Driver profile not found for ID: " + command.driverId());
            }
            
            // Check if a vehicle with the same license plate already exists
            if (vehicleRepository.existsByLicensePlate(command.licensePlate())) {
                throw new IllegalArgumentException("Vehicle with license plate '" + command.licensePlate() + "' already exists");
            }
            
            var vehicle = new Vehicle(command);
            var vehicleCreated = vehicleRepository.save(vehicle);
            
            logger.info("Successfully created vehicle with ID: {} for driver: {}", 
                       vehicleCreated.getId(), command.driverId());
            
            return Optional.of(vehicleCreated);
            
        } catch (Exception e) {
            logger.error("Failed to create vehicle for driver: {} - Error: {}", 
                        command.driverId(), e.getMessage());
            throw e; // Re-throw to maintain transactional behavior
        }
    }

    // {@inheritDoc}
    @Override
    public Optional<Vehicle> handle(UpdateVehicleCommand command) {
        logger.info("Processing vehicle update for vehicleId: {} with new license plate: {}", 
                   command.vehicleId(), command.licensePlate());

        try {
            var vehicleId = command.vehicleId();
            var vehicle = this.vehicleRepository.findById(vehicleId)
                    .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with ID: " + command.vehicleId()));
                    
            // Check for license plate conflicts (if changed)
            if (!vehicle.getLicensePlate().equals(command.licensePlate()) && 
                vehicleRepository.existsByLicensePlate(command.licensePlate())) {
                throw new IllegalArgumentException("Vehicle with license plate '" + command.licensePlate() + "' already exists");
            }
            
            vehicle.updateVehicle(command.licensePlate(), command.brand(), command.model());
            var vehicleUpdated = vehicleRepository.save(vehicle);
            
            logger.info("Successfully updated vehicle with ID: {}", vehicleId);
            
            return Optional.of(vehicleUpdated);
            
        } catch (Exception e) {
            logger.error("Failed to update vehicle with ID: {} - Error: {}", 
                        command.vehicleId(), e.getMessage());
            throw e; // Re-throw to maintain transactional behavior
        }
    }

    // {@inheritDoc}
    @Override
    public void handle(DeleteVehicleCommand command) {
        logger.info("Processing vehicle deletion for vehicleId: {}", command.vehicleId());

        try {
            var exists = this.vehicleRepository.existsById(command.vehicleId());
            if (!exists)
                throw new IllegalArgumentException("Vehicle not found with ID: " + command.vehicleId());
                
            // Note: Future enhancement could include validation to check if vehicle has active
            // workshop operations before allowing deletion through workshop ACL integration
            
            this.vehicleRepository.deleteById(command.vehicleId());
            
            logger.info("Successfully deleted vehicle with ID: {}", command.vehicleId());
            
        } catch (Exception e) {
            logger.error("Failed to delete vehicle with ID: {} - Error: {}", 
                        command.vehicleId(), e.getMessage());
            throw e; // Re-throw to maintain transactional behavior
        }
    }
}