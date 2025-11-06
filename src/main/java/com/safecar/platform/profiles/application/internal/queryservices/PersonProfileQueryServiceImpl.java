package com.safecar.platform.profiles.application.internal.queryservices;

import com.safecar.platform.profiles.domain.model.aggregates.PersonProfile;
import com.safecar.platform.profiles.domain.services.PersonProfileQueryService;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.PersonProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonProfileQueryServiceImpl implements PersonProfileQueryService {
    private final PersonProfileRepository personProfileRepository;

    public PersonProfileQueryServiceImpl(PersonProfileRepository personProfileRepository) {
        this.personProfileRepository = personProfileRepository;
    }

    @Override
    public Optional<PersonProfile> findByUserId(Long userId) {
        return personProfileRepository.findByUserId(userId);
    }

    @Override
    public Optional<PersonProfile> findById(Long id) {
        return personProfileRepository.findById(id);
    }
}
