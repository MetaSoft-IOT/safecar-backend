package com.safecar.platform.profiles.application.internal.commandservices;

import com.safecar.platform.profiles.domain.model.commands.CreatePersonProfileCommand;
import com.safecar.platform.profiles.domain.services.PersonProfileCommandService;
import com.safecar.platform.workshop.interfaces.acl.WorkshopContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Workshop Mechanic Profile Orchestrator Service
 * <p>
 * Coordinates the creation of workshop mechanics across bounded contexts:
 * 1. Creates PersonProfile in Profiles BC
 * 2. Creates Mechanic in Workshop BC using the PersonProfile ID
 * </p>
 */
@Service
public class WorkshopMechanicProfileOrchestratorService {
    private final PersonProfileCommandService personProfileCommandService;
    private final WorkshopContextFacade workshopContextFacade;

    public WorkshopMechanicProfileOrchestratorService(PersonProfileCommandService personProfileCommandService,
                                                      WorkshopContextFacade workshopContextFacade) {
        this.personProfileCommandService = personProfileCommandService;
        this.workshopContextFacade = workshopContextFacade;
    }

    /**
     * Creates a complete workshop mechanic profile by coordinating between Profiles and Workshop BCs
     */
    public Optional<Long> createWorkshopMechanicProfile(String fullName, String city, String country,
                                                        String phone, String dni, String companyName,
                                                        String specializations, Integer yearsOfExperience,
                                                        Long userId) {
        try {
            // Step 1: Create PersonProfile in Profiles BC
            var personProfileCommand = new CreatePersonProfileCommand(fullName, city, country, phone, dni);
            var personProfile = personProfileCommandService.handle(personProfileCommand, userId);
            
            if (personProfile.isEmpty()) {
                return Optional.empty();
            }
            
            Long personProfileId = personProfile.get().getId();
            
            // Step 2: Create Mechanic in Workshop BC
            Long mechanicId = workshopContextFacade.createMechanic(
                    personProfileId, companyName, specializations, yearsOfExperience);
            
            return mechanicId > 0 ? Optional.of(mechanicId) : Optional.empty();
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create workshop mechanic profile: " + e.getMessage(), e);
        }
    }
}