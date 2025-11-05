package com.safecar.platform.profiles.interfaces.acl;

/**
 * Anti-Corruption Layer (ACL) for Profiles Bounded Context.
 * 
 * This facade provides a stable interface for external bounded contexts
 * to interact with the Profiles domain without coupling to internal implementation details.
 */
public interface ProfilesContextFacade {

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
    Long createDriver(String fullName, String city, String country,
            String phone, String dni, Long userId);

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
    Long createWorkshopMechanic(String fullName, String city, String country,
            String phone, String companyName, String dni, Long userId);

    /**
     * Checks if a driver profile exists for the given user ID.
     *
     * @param userId the user ID to check
     * @return true if driver profile exists, false otherwise
     */
    boolean existsDriverByUserId(Long userId);

    /**
     * Checks if a workshop mechanic profile exists for the given user ID.
     *
     * @param userId the user ID to check
     * @return true if workshop mechanic profile exists, false otherwise
     */
    boolean existsWorkshopMechanicByUserId(Long userId);

    /**
     * Retrieves the driver ID associated with a user ID.
     *
     * @param userId the user ID
     * @return the driver ID if found, 0 if not found
     */
    Long getDriverIdByUserId(Long userId);

    /**
     * Retrieves the workshop mechanic ID associated with a user ID.
     *
     * @param userId the user ID
     * @return the workshop mechanic ID if found, 0 if not found
     */
    Long getWorkshopMechanicIdByUserId(Long userId);
}
