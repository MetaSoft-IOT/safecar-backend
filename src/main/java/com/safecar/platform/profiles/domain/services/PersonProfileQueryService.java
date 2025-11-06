package com.safecar.platform.profiles.domain.services;

import com.safecar.platform.profiles.domain.model.aggregates.PersonProfile;

import java.util.Optional;

public interface PersonProfileQueryService {
    Optional<PersonProfile> findByUserId(Long userId);
    Optional<PersonProfile> findById(Long id);
}
