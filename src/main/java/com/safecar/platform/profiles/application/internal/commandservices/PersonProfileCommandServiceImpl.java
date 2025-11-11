package com.safecar.platform.profiles.application.internal.commandservices;

import com.safecar.platform.profiles.application.internal.outbounceservices.acl.ExternalIamService;
import com.safecar.platform.profiles.domain.model.aggregates.PersonProfile;
import com.safecar.platform.profiles.domain.model.commands.CreatePersonProfileCommand;
import com.safecar.platform.profiles.domain.model.commands.UpdatePersonProfileCommand;
import com.safecar.platform.profiles.domain.model.valueobjects.Dni;
import com.safecar.platform.profiles.domain.model.valueobjects.Phone;
import com.safecar.platform.profiles.domain.services.PersonProfileCommandService;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.PersonProfileRepository;
import com.safecar.platform.shared.domain.model.events.ProfileCreatedEvent;

import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of PersonProfileCommandService
 */
@Service
public class PersonProfileCommandServiceImpl implements PersonProfileCommandService {

    /**
     * The PersonProfile repository
     */
    private final PersonProfileRepository personProfileRepository;
    /**
     * The Application Event Publisher
     */
    private final ApplicationEventPublisher applicationEventPublisher;
    /**
     * The External IAM Service
     */
    private final ExternalIamService externalIamService;

    public PersonProfileCommandServiceImpl(
            PersonProfileRepository personProfileRepository,
            ApplicationEventPublisher applicationEventPublisher,
            ExternalIamService externalIamService) {
        this.personProfileRepository = personProfileRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.externalIamService = externalIamService;
    }

    // javadoc inherited
    @Transactional
    @Override
    public Optional<PersonProfile> handle(CreatePersonProfileCommand command, String userEmail) {

        var userRoles = externalIamService.fetchUserRolesByUserEmail(userEmail);

        var profile = new PersonProfile(
                userEmail,
                command.fullName(),
                command.city(),
                command.country(),
                new Phone(command.phone()),
                new Dni(command.dni()));

        var saved = personProfileRepository.save(profile);

        var event = new ProfileCreatedEvent(
                saved.getId(),
                userRoles);

        applicationEventPublisher.publishEvent(event);

        return Optional.of(saved);
    }

    // javadoc inherited
    @Transactional
    @Override
    public Optional<PersonProfile> handle(UpdatePersonProfileCommand command, Long personProfileId) {

        var personProfile = personProfileRepository.findById(personProfileId);

        if (!personProfile.isPresent())
            throw new IllegalArgumentException("PersonProfile with id " + command.personId() + " not found");

        var entity = personProfile.get();

        entity.updatePersonProfileMetrics(
                command.fullName(),
                command.city(),
                command.country(),
                command.phone(),
                command.dni());

        var updated = personProfileRepository.save(entity);

        return Optional.of(updated);
    }
}
