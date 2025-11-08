package com.safecar.platform.workshop.interfaces.acl;

import java.util.List;

/**
 * Anti-Corruption Layer (ACL) for Workshop Bounded Context.
 * 
 * This facade provides a stable interface for external bounded contexts
 * to interact with the Workshop domain without coupling to internal implementation details.
 */
public interface WorkshopContextFacade {

    /**
     * Creates a new mechanic with workshop-specific data.
     *
     * @param profileId the ID of the PersonProfile from Profiles BC
     * @param companyName name of the workshop company
     * @param specializations mechanic's technical specializations as list (optional)
     * @param yearsOfExperience years of professional experience (optional)
     * @return the ID of the created mechanic, 0 if creation failed
     */
    Long createMechanic(Long profileId, String companyName, List<String> specializations, Integer yearsOfExperience);

    /**
     * Checks if a mechanic exists for the given profile ID.
     *
     * @param profileId the profile ID from Profiles BC
     * @return true if mechanic exists, false otherwise
     */
    boolean existsMechanicByProfileId(Long profileId);

    /**
     * Retrieves the mechanic ID associated with a profile ID.
     *
     * @param profileId the profile ID from Profiles BC
     * @return the mechanic ID if found, 0 if not found
     */
    Long getMechanicIdByProfileId(Long profileId);
}