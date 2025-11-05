package com.safecar.platform.devices.application.internal.outboundservices.acl;

import com.safecar.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * External Profile Service (Anti-Corruption Layer).
 * 
 * This service acts as an ACL between the Devices and Profiles bounded contexts,
 * providing vehicle-specific operations that interact with driver profiles.
 * It translates between the domains and ensures loose coupling.
 * 
 * Now fully integrated with ProfilesContextFacade for complete profile operations.
 */
@Service
public class ExternalProfileService {
    
    private static final Logger logger = LoggerFactory.getLogger(ExternalProfileService.class);
    
    private final ProfilesContextFacade profilesContextFacade;

    /**
     * Constructor for ExternalProfileService.
     * 
     * @param profilesContextFacade the profiles context facade for ACL operations
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
        try {
            // Basic validation - check if driverId is valid
            if (driverId == null || driverId <= 0) {
                logger.error("Invalid driver ID provided: {}", driverId);
                throw new IllegalArgumentException("Invalid driver ID: " + driverId);
            }
            
            // Enhanced validation using ProfilesContextFacade
            boolean driverExists = profilesContextFacade.existsDriverByUserId(driverId);
            if (!driverExists) {
                logger.error("Driver not found for ID: {}", driverId);
                throw new IllegalArgumentException("Driver not found for ID: " + driverId);
            }
            
            logger.debug("Driver validation successful for ID: {}", driverId);
        } catch (Exception e) {
            logger.error("Error validating driver with ID {}: {}", driverId, e.getMessage());
            throw e;
        }
    }

    /**
     * Validates that a driver exists by user ID.
     * 
     * @param userId the user ID to validate
     * @return true if driver profile exists, false otherwise
     */
    public boolean validateDriverExistsByUserId(Long userId) {
        try {
            if (userId == null || userId <= 0) {
                logger.warn("Invalid user ID provided: {}", userId);
                return false;
            }
            return profilesContextFacade.existsDriverByUserId(userId);
        } catch (Exception e) {
            logger.error("Error validating driver existence for user ID {}: {}", userId, e.getMessage());
            return false;
        }
    }

    /**
     * Gets the driver ID associated with a user ID.
     * 
     * @param userId the user ID
     * @return the driver ID if found, 0 if not found
     */
    public Long getDriverIdByUserId(Long userId) {
        try {
            if (userId == null || userId <= 0) {
                logger.warn("Invalid user ID provided: {}", userId);
                return 0L;
            }
            return profilesContextFacade.getDriverIdByUserId(userId);
        } catch (Exception e) {
            logger.error("Error retrieving driver ID for user ID {}: {}", userId, e.getMessage());
            return 0L;
        }
    }

    /**
     * Checks if a workshop mechanic profile exists for the given user ID.
     * 
     * @param userId the user ID to check
     * @return true if workshop mechanic profile exists, false otherwise
     */
    public boolean existsWorkshopMechanicByUserId(Long userId) {
        try {
            if (userId == null || userId <= 0) {
                logger.warn("Invalid user ID provided: {}", userId);
                return false;
            }
            return profilesContextFacade.existsWorkshopMechanicByUserId(userId);
        } catch (Exception e) {
            logger.error("Error validating workshop mechanic existence for user ID {}: {}", userId, e.getMessage());
            return false;
        }
    }

    /**
     * Gets the workshop mechanic ID associated with a user ID.
     * 
     * @param userId the user ID
     * @return the workshop mechanic ID if found, 0 if not found
     */
    public Long getWorkshopMechanicIdByUserId(Long userId) {
        try {
            if (userId == null || userId <= 0) {
                logger.warn("Invalid user ID provided: {}", userId);
                return 0L;
            }
            return profilesContextFacade.getWorkshopMechanicIdByUserId(userId);
        } catch (Exception e) {
            logger.error("Error retrieving workshop mechanic ID for user ID {}: {}", userId, e.getMessage());
            return 0L;
        }
    }

    /**
     * Creates a new driver profile.
     * 
     * @param fullName full name of the driver
     * @param city city where the driver resides
     * @param country country where the driver resides
     * @param phone contact phone number
     * @param dni identification document number
     * @param userId associated user account ID
     * @return the ID of the created driver profile, 0 if creation failed
     */
    public Long createDriverProfile(String fullName, String city, String country,
                                   String phone, String dni, Long userId) {
        try {
            if (userId == null || userId <= 0) {
                logger.error("Invalid user ID provided for driver creation: {}", userId);
                return 0L;
            }
            
            Long driverId = profilesContextFacade.createDriver(fullName, city, country, phone, dni, userId);
            logger.info("Successfully created driver profile with ID {} for user ID {}", driverId, userId);
            return driverId;
        } catch (Exception e) {
            logger.error("Error creating driver profile for user ID {}: {}", userId, e.getMessage());
            return 0L;
        }
    }

    /**
     * Creates a new workshop mechanic profile.
     * 
     * @param fullName full name of the workshop mechanic
     * @param city city where the workshop is located
     * @param country country where the workshop is located
     * @param phone contact phone number
     * @param companyName name of the workshop company
     * @param dni identification/tax document number
     * @param userId associated user account ID
     * @return the ID of the created workshop mechanic profile, 0 if creation failed
     */
    public Long createWorkshopMechanicProfile(String fullName, String city, String country,
                                             String phone, String companyName, String dni, Long userId) {
        try {
            if (userId == null || userId <= 0) {
                logger.error("Invalid user ID provided for workshop mechanic creation: {}", userId);
                return 0L;
            }
            
            Long mechanicId = profilesContextFacade.createWorkshopMechanic(
                fullName, city, country, phone, companyName, dni, userId);
            logger.info("Successfully created workshop mechanic profile with ID {} for user ID {}", mechanicId, userId);
            return mechanicId;
        } catch (Exception e) {
            logger.error("Error creating workshop mechanic profile for user ID {}: {}", userId, e.getMessage());
            return 0L;
        }
    }

    /**
     * Validates user profile type and determines what operations they can perform.
     * 
     * @param userId the user ID
     * @return profile type description ("DRIVER", "WORKSHOP_MECHANIC", "BOTH", "NONE")
     */
    public String validateUserProfileType(Long userId) {
        try {
            if (userId == null || userId <= 0) {
                logger.warn("Invalid user ID provided: {}", userId);
                return "NONE";
            }
            
            boolean isDriver = profilesContextFacade.existsDriverByUserId(userId);
            boolean isMechanic = profilesContextFacade.existsWorkshopMechanicByUserId(userId);
            
            if (isDriver && isMechanic) {
                return "BOTH";
            } else if (isDriver) {
                return "DRIVER";
            } else if (isMechanic) {
                return "WORKSHOP_MECHANIC";
            } else {
                return "NONE";
            }
        } catch (Exception e) {
            logger.error("Error validating profile type for user ID {}: {}", userId, e.getMessage());
            return "NONE";
        }
    }

    /**
     * Determines if user can perform vehicle operations (typically drivers).
     * 
     * @param userId the user ID
     * @return true if user can perform vehicle operations, false otherwise
     */
    public boolean canPerformVehicleOperations(Long userId) {
        try {
            return profilesContextFacade.existsDriverByUserId(userId);
        } catch (Exception e) {
            logger.error("Error checking vehicle operation permissions for user ID {}: {}", userId, e.getMessage());
            return false;
        }
    }

    /**
     * Determines if user can perform workshop operations (typically mechanics).
     * 
     * @param userId the user ID
     * @return true if user can perform workshop operations, false otherwise
     */
    public boolean canPerformWorkshopOperations(Long userId) {
        try {
            return profilesContextFacade.existsWorkshopMechanicByUserId(userId);
        } catch (Exception e) {
            logger.error("Error checking workshop operation permissions for user ID {}: {}", userId, e.getMessage());
            return false;
        }
    }


}
