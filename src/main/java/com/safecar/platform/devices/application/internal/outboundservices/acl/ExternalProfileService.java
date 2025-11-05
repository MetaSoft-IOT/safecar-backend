package com.safecar.platform.devices.application.internal.outboundservices.acl;

import com.safecar.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;

/**
 * External Profile Service (Anti-Corruption Layer).
 * 
 * This service acts as an ACL between the Devices and Profiles bounded contexts,
 * providing vehicle-specific operations that interact with driver profiles.
 * It translates between the domains and ensures loose coupling.
 */
@Service
public class ExternalProfileService {
    
    private final ProfilesContextFacade profilesContextFacade;

    /**
     * Constructor for ExternalProfileService.
     * 
     * @param profilesContextFacade the profiles context facade
     */
    public ExternalProfileService(ProfilesContextFacade profilesContextFacade) {
        this.profilesContextFacade = profilesContextFacade;
    }

    /**
     * Validates that a driver exists before creating a vehicle.
     * 
     * @param driverId the driver ID to validate
     * @throws IllegalArgumentException if the driver does not exist
     */
    public void validateDriverExists(Long driverId) {
        // Note: driverId is the profile ID, not userId
        // We need to validate through userId if we have it, or implement getDriverById
        // For now, we assume the driverId is valid if > 0
        if (driverId == null || driverId <= 0) {
            throw new IllegalArgumentException("Invalid driver ID: " + driverId);
        }
        // TODO: Implement proper driver validation when we have getDriverById in ACL
    }

    /**
     * Validates that a driver exists by user ID.
     * 
     * @param userId the user ID to check
     * @return true if driver exists
     * @throws IllegalArgumentException if the driver does not exist
     */
    public boolean validateDriverExistsByUserId(Long userId) {
        boolean exists = profilesContextFacade.existsDriverByUserId(userId);
        if (!exists) {
            throw new IllegalArgumentException("Driver not found for user ID: " + userId);
        }
        return true;
    }

    /**
     * Gets the driver ID associated with a user ID.
     * 
     * @param userId the user ID
     * @return the driver ID
     * @throws IllegalArgumentException if no driver found for the user
     */
    public Long getDriverIdByUserId(Long userId) {
        Long driverId = profilesContextFacade.getDriverIdByUserId(userId);
        if (driverId == 0L) {
            throw new IllegalArgumentException("No driver profile found for user ID: " + userId);
        }
        return driverId;
    }

    /**
     * Checks if a driver exists by user ID without throwing exceptions.
     * 
     * @param userId the user ID to check
     * @return true if driver exists, false otherwise
     */
    public boolean existsDriverByUserId(Long userId) {
        return profilesContextFacade.existsDriverByUserId(userId);
    }

    /**
     * Checks if a workshop mechanic exists by user ID.
     * 
     * @param userId the user ID to check
     * @return true if workshop mechanic exists, false otherwise
     */
    public boolean existsWorkshopMechanicByUserId(Long userId) {
        return profilesContextFacade.existsWorkshopMechanicByUserId(userId);
    }

    /**
     * Gets the workshop mechanic ID associated with a user ID.
     * 
     * @param userId the user ID
     * @return the workshop mechanic ID
     * @throws IllegalArgumentException if no workshop mechanic found for the user
     */
    public Long getWorkshopMechanicIdByUserId(Long userId) {
        Long workshopMechanicId = profilesContextFacade.getWorkshopMechanicIdByUserId(userId);
        if (workshopMechanicId == 0L) {
            throw new IllegalArgumentException("No workshop mechanic profile found for user ID: " + userId);
        }
        return workshopMechanicId;
    }
}
