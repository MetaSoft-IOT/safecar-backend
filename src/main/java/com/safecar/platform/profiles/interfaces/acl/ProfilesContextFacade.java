package com.safecar.platform.profiles.interfaces.acl;

/**
 * Anti-Corruption Layer (ACL) for Profiles Bounded Context.
 * 
 * This facade provides a stable interface for external bounded contexts
 * to interact with the Profiles domain without coupling to internal implementation details.
 */
public interface ProfilesContextFacade {
        /**
         * Creates a new person profile (generic person representation used by other BCs).
         *
         * @param fullName person's full name
         * @param city city where the person resides
         * @param country country where the person resides
         * @param phone contact phone number
         * @param dni identification document number
         * @param userId associated user account ID
         * @return the ID of the created person profile, 0 if creation failed
         */
        Long createPersonProfile(String fullName, String city, String country,
                                                         String phone, String dni, Long userId);

        /**
         * Checks if a person profile exists for the given user ID.
         *
         * @param userId the user ID to check
         * @return true if person profile exists, false otherwise
         */
        boolean existsPersonProfileByUserId(Long userId);

        /**
         * Checks if a person profile exists for the given profile ID.
         *
         * @param profileId the person profile ID to check
         * @return true if person profile exists, false otherwise
         */
        boolean existsPersonProfileById(Long profileId);

        /**
         * Retrieves the person profile ID associated with a user ID.
         *
         * @param userId the user ID
         * @return the person profile ID if found, 0 if not found
         */
        Long getPersonProfileIdByUserId(Long userId);
}
