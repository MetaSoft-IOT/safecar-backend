package com.safecar.platform.profiles.interfaces.acl;

/**
 * Anti-Corruption Layer (ACL) for Profiles Bounded Context.
 * 
 * This facade provides a stable interface for external bounded contexts
 * to interact with the Profiles domain without coupling to internal
 * implementation details.
 */
public interface ProfilesContextFacade {

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
