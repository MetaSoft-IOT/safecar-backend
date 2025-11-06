package com.safecar.platform.profiles.application.internal.commandservices;

import com.safecar.platform.profiles.domain.model.commands.CreatePersonProfileCommand;
import com.safecar.platform.profiles.domain.services.PersonProfileCommandService;
import com.safecar.platform.devices.interfaces.acl.DevicesContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Driver Profile Orchestrator Service
 * <p>
 * Coordinates the creation of drivers across bounded contexts:
 * 1. Creates PersonProfile in Profiles BC
 * 2. Creates Driver in Devices BC using the PersonProfile ID
 * </p>
 */
@Service
public class DriverProfileOrchestratorService {
    private final PersonProfileCommandService personProfileCommandService;
    private final DevicesContextFacade devicesContextFacade;

    public DriverProfileOrchestratorService(PersonProfileCommandService personProfileCommandService,
                                            DevicesContextFacade devicesContextFacade) {
        this.personProfileCommandService = personProfileCommandService;
        this.devicesContextFacade = devicesContextFacade;
    }

    /**
     * Creates a complete driver profile by coordinating between Profiles and Devices BCs
     */
    public Optional<Long> createDriverProfile(String fullName, String city, String country,
                                              String phone, String dni, Long userId) {
        try {
            // Step 1: Create PersonProfile in Profiles BC
            var personProfileCommand = new CreatePersonProfileCommand(fullName, city, country, phone, dni);
            var personProfile = personProfileCommandService.handle(personProfileCommand, userId);
            
            if (personProfile.isEmpty()) {
                return Optional.empty();
            }
            
            Long personProfileId = personProfile.get().getId();
            
            // Step 2: Create Driver in Devices BC
            Long driverId = devicesContextFacade.createDriver(personProfileId);
            
            return driverId > 0 ? Optional.of(driverId) : Optional.empty();
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create driver profile: " + e.getMessage(), e);
        }
    }
}