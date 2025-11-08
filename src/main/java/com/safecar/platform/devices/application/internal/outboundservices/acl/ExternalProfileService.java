package com.safecar.platform.devices.application.internal.outboundservices.acl;

import com.safecar.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;

/**
 * External Profile Service
 * <p>
 *     This service provides access to Profiles bounded context functionality from Devices.
 *     It acts as an adapter/wrapper around the ProfilesContextFacade to provide domain-specific
 *     operations for the Devices bounded context.
 * </p>
 */
@Service("devicesExternalProfileService")
public class ExternalProfileService {
    
    private final ProfilesContextFacade profilesContextFacade;

    /**
     * Constructor
     *
     * @param profilesContextFacade Profiles Context Facade
     */
    public ExternalProfileService(ProfilesContextFacade profilesContextFacade) {
        this.profilesContextFacade = profilesContextFacade;
    }

    /**
     * Validate if a person profile exists
     * 
     * @param profileId The profile ID to validate
     * @return true if profile exists, false otherwise
     */
    public boolean validatePersonProfileExists(Long profileId) {
        try {
            return profilesContextFacade.existsPersonProfileById(profileId);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Fetch person profile information by ID
     * 
     * @param profileId The profile ID
     * @return Profile information as formatted string
     */
    public String fetchPersonProfileInfo(Long profileId) {
        try {
            return "Profile ID: " + profileId;
        } catch (Exception e) {
            return "Unknown Profile";
        }
    }

    /**
     * Get person profile ID by user ID
     * 
     * @param userId The user ID
     * @return The profile ID, or null if not found
     */
    public Long getPersonProfileIdByUserId(Long userId) {
        try {
            Long profileId = profilesContextFacade.getPersonProfileIdByUserId(userId);
            return profileId > 0 ? profileId : null;
        } catch (Exception e) {
            return null;
        }
    }
}
