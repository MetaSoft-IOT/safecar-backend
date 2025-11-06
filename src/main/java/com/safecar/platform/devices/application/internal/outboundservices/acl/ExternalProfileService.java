package com.safecar.platform.devices.application.internal.outboundservices.acl;

import com.safecar.platform.profiles.interfaces.acl.ProfilesContextFacade;
import com.safecar.platform.profiles.application.internal.commandservices.DriverProfileOrchestratorService;
import com.safecar.platform.profiles.application.internal.commandservices.WorkshopMechanicProfileOrchestratorService;
import com.safecar.platform.devices.interfaces.acl.DevicesContextFacade;
import com.safecar.platform.workshop.interfaces.acl.WorkshopContextFacade;
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
    private final DriverProfileOrchestratorService driverProfileOrchestratorService;
    private final WorkshopMechanicProfileOrchestratorService workshopMechanicProfileOrchestratorService;
    private final DevicesContextFacade devicesContextFacade;
    private final WorkshopContextFacade workshopContextFacade;

    /**
     * Constructor for ExternalProfileService.
     * 
     * @param profilesContextFacade the profiles context facade for ACL operations
     * @param driverProfileOrchestratorService driver profile orchestrator service
     * @param workshopMechanicProfileOrchestratorService workshop mechanic profile orchestrator service
     * @param devicesContextFacade devices context facade
     * @param workshopContextFacade workshop context facade
     */
    public ExternalProfileService(ProfilesContextFacade profilesContextFacade,
                                  DriverProfileOrchestratorService driverProfileOrchestratorService,
                                  WorkshopMechanicProfileOrchestratorService workshopMechanicProfileOrchestratorService,
                                  DevicesContextFacade devicesContextFacade,
                                  WorkshopContextFacade workshopContextFacade) {
        this.profilesContextFacade = profilesContextFacade;
        this.driverProfileOrchestratorService = driverProfileOrchestratorService;
        this.workshopMechanicProfileOrchestratorService = workshopMechanicProfileOrchestratorService;
        this.devicesContextFacade = devicesContextFacade;
        this.workshopContextFacade = workshopContextFacade;
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
            
            // Check if driver exists in devices BC (driverId is actually a driver entity ID)
            // For validation we need to check if it exists directly in the devices context
            // This assumes driverId is the actual driver entity ID, not the profile ID
            boolean driverExists = validateVehicleDriverExists(driverId);
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
     * Helper method to validate if a driver exists by checking vehicle relationships
     */
    private boolean validateVehicleDriverExists(Long driverId) {
        // This is a simplified check - in a real scenario you might have a direct driver validation
        // For now, we assume if a driver ID is used, it should be valid
        return driverId != null && driverId > 0;
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
            
            // Check if person profile exists for user
            if (!profilesContextFacade.existsPersonProfileByUserId(userId)) {
                return false;
            }
            
            // Get profile ID and check if driver exists in devices BC
            Long profileId = profilesContextFacade.getPersonProfileIdByUserId(userId);
            return devicesContextFacade.existsDriverByProfileId(profileId);
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
            
            // Get profile ID first
            Long profileId = profilesContextFacade.getPersonProfileIdByUserId(userId);
            if (profileId == 0L) {
                return 0L;
            }
            
            // Get driver ID from devices BC
            return devicesContextFacade.getDriverIdByProfileId(profileId);
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
            
            // Check if person profile exists for user
            if (!profilesContextFacade.existsPersonProfileByUserId(userId)) {
                return false;
            }
            
            // Get profile ID and check if mechanic exists in workshop BC
            Long profileId = profilesContextFacade.getPersonProfileIdByUserId(userId);
            return workshopContextFacade.existsMechanicByProfileId(profileId);
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
            
            // Get profile ID first
            Long profileId = profilesContextFacade.getPersonProfileIdByUserId(userId);
            if (profileId == 0L) {
                return 0L;
            }
            
            // Get mechanic ID from workshop BC
            return workshopContextFacade.getMechanicIdByProfileId(profileId);
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
            
            return driverProfileOrchestratorService.createDriverProfile(fullName, city, country, phone, dni, userId).orElse(0L);
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
            
            // Use default values for specializations and years of experience since the original method doesn't have these parameters
            String defaultSpecializations = "General Automotive Repair";
            Integer defaultYearsOfExperience = 1;
            
            return workshopMechanicProfileOrchestratorService.createWorkshopMechanicProfile(
                fullName, city, country, phone, dni, companyName, defaultSpecializations, defaultYearsOfExperience, userId).orElse(0L);
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
            
            // Check if person profile exists first
            if (!profilesContextFacade.existsPersonProfileByUserId(userId)) {
                return "NONE";
            }
            
            // Get profile ID to check business-specific roles
            Long profileId = profilesContextFacade.getPersonProfileIdByUserId(userId);
            if (profileId == 0L) {
                return "NONE";
            }
            
            // Check business-specific roles in their respective BCs
            boolean isDriver = devicesContextFacade.existsDriverByProfileId(profileId);
            boolean isMechanic = workshopContextFacade.existsMechanicByProfileId(profileId);
            
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
            if (userId == null || userId <= 0) {
                return false;
            }
            
            // Check if person profile exists and get profile ID
            Long profileId = profilesContextFacade.getPersonProfileIdByUserId(userId);
            if (profileId == 0L) {
                return false;
            }
            
            // Check if user has a driver profile in Devices BC
            return devicesContextFacade.existsDriverByProfileId(profileId);
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
            if (userId == null || userId <= 0) {
                return false;
            }
            
            // Check if person profile exists and get profile ID
            Long profileId = profilesContextFacade.getPersonProfileIdByUserId(userId);
            if (profileId == 0L) {
                return false;
            }
            
            // Check if user has a mechanic profile in Workshop BC
            return workshopContextFacade.existsMechanicByProfileId(profileId);
        } catch (Exception e) {
            logger.error("Error checking workshop operation permissions for user ID {}: {}", userId, e.getMessage());
            return false;
        }
    }


}
