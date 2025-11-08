package com.safecar.platform.profiles.application.internal.commandservices;

import com.safecar.platform.profiles.application.internal.outbounceservices.acl.ExternalIamService;
import com.safecar.platform.profiles.domain.model.aggregates.PersonProfile;
import com.safecar.platform.profiles.domain.model.commands.CreatePersonProfileCommand;
import com.safecar.platform.profiles.domain.model.valueobjects.Dni;
import com.safecar.platform.profiles.domain.model.valueobjects.Phone;
import com.safecar.platform.profiles.domain.services.PersonProfileCommandService;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.PersonProfileRepository;
import com.safecar.platform.shared.domain.model.events.PersonProfileCreatedEvent;

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

    /**
     * Handles the creation of a new PersonProfile.
     * <p>
     * Publishes a {@link PersonProfileCreatedEvent} after successful creation.
     * </p>
     * 
     * @param command the {@link CreatePersonProfileCommand} instance
     * @return the created {@link PersonProfile} instance
     */
    @Transactional
    @Override
    public PersonProfile handle(CreatePersonProfileCommand command, Long userId) {

        var userRoles = externalIamService.fetchUserRolesByUserId(userId);

        var profile = new PersonProfile(
                userId,
                command.fullName(),
                command.city(),
                command.country(),
                new Phone(command.phone()),
                new Dni(command.dni()));

        var savedProfile = personProfileRepository.save(profile);

        var event = new PersonProfileCreatedEvent(
                savedProfile.getId(),
                userRoles);

        applicationEventPublisher.publishEvent(event);

        return savedProfile;
    }
}
