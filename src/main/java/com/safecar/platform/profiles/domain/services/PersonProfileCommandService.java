package com.safecar.platform.profiles.domain.services;

import com.safecar.platform.profiles.domain.model.aggregates.PersonProfile;
import com.safecar.platform.profiles.domain.model.commands.CreatePersonProfileCommand;

import java.util.Optional;

public interface PersonProfileCommandService {
    Optional<PersonProfile> handle(CreatePersonProfileCommand command, Long userId);
}
