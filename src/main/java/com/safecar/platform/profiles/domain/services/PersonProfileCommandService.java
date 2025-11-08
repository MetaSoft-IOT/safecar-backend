package com.safecar.platform.profiles.domain.services;

import com.safecar.platform.profiles.domain.model.aggregates.PersonProfile;
import com.safecar.platform.profiles.domain.model.commands.CreatePersonProfileCommand;

/**
 * Person Profile Command Service
 * <p>
 * Service interface for handling commands related to Person Profiles.
 * </p>
 */
public interface PersonProfileCommandService {
    /**
     * Handles the creation of a Person Profile.
     * 
     * @param command the {@link CreatePersonProfileCommand} containing profile details
     * @param userId  the ID of the user for whom the profile is being created
     * @return the created Person Profile
     */
    PersonProfile handle(CreatePersonProfileCommand command, Long userId);
}
