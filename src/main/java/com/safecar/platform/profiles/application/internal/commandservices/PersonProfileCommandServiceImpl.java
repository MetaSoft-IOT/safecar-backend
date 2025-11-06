package com.safecar.platform.profiles.application.internal.commandservices;

import com.safecar.platform.profiles.domain.model.aggregates.PersonProfile;
import com.safecar.platform.profiles.domain.model.commands.CreatePersonProfileCommand;
import com.safecar.platform.profiles.domain.services.PersonProfileCommandService;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.PersonProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonProfileCommandServiceImpl implements PersonProfileCommandService {
    private final PersonProfileRepository personProfileRepository;

    public PersonProfileCommandServiceImpl(PersonProfileRepository personProfileRepository) {
        this.personProfileRepository = personProfileRepository;
    }

    @Override
    public Optional<PersonProfile> handle(CreatePersonProfileCommand command, Long userId) {
        if (personProfileRepository.existsByPhone_Phone(command.phone())) {
            throw new IllegalArgumentException("Phone already exists");
        }

        if (personProfileRepository.existsByDni_Dni(command.dni())) {
            throw new IllegalArgumentException("DNI already exists");
        }

        var person = new PersonProfile(command, userId);
        var created = personProfileRepository.save(person);
        return Optional.of(created);
    }
}
