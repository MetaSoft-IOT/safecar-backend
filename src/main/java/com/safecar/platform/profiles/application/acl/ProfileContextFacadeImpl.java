package com.safecar.platform.profiles.application.acl;

import com.safecar.platform.profiles.domain.model.commands.CreatePersonProfileCommand;
import com.safecar.platform.profiles.domain.model.queries.GetPersonProfileByIdQuery;
import com.safecar.platform.profiles.domain.model.queries.GetPersonProfileByUserIdQuery;
import com.safecar.platform.profiles.domain.services.PersonProfileCommandService;
import com.safecar.platform.profiles.domain.services.PersonProfileQueryService;
import com.safecar.platform.profiles.interfaces.acl.ProfilesContextFacade;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import org.springframework.stereotype.Service;

/**
 * Implementation of the Profiles Context Facade (Anti-Corruption Layer).
 * 
 * This service provides a stable interface for external bounded contexts
 * to interact with the Profiles domain, ensuring loose coupling and
 * protecting internal domain model changes.
 */
@Service
public class ProfileContextFacadeImpl implements ProfilesContextFacade {
    private final PersonProfileQueryService personProfileQueryService;

    /**
     * Constructor for ProfileContextFacadeImpl.
     *
     * @param driverCommandService           driver command service
     * @param driverQueryService             driver query service
     * @param workshopMechanicCommandService workshop mechanic command service
     * @param workshopMechanicQueryService   workshop mechanic query service
     */
    public ProfileContextFacadeImpl(PersonProfileQueryService personProfileQueryService) {
        this.personProfileQueryService = personProfileQueryService;
    }

    // inherited javadoc
    @Override
    public boolean existsPersonProfileByUserId(Long userId) {
        var getUserProfileQuery = new GetPersonProfileByUserIdQuery(userId);
        var existing = personProfileQueryService.handle(getUserProfileQuery);
        return existing.isPresent();
    }

    // inherited javadoc
    @Override
    public boolean existsPersonProfileById(Long profileId) {
        var getProfileByIdQuery = new GetPersonProfileByIdQuery(profileId);
        var existing = personProfileQueryService.handle(getProfileByIdQuery);
        return existing.isPresent();
    }

    // inherited javadoc
    @Override
    public Long getPersonProfileIdByUserId(Long userId) {
        var getUserProfileQuery = new GetPersonProfileByUserIdQuery(userId);
        var person = personProfileQueryService.handle(getUserProfileQuery);
        return person.map(AuditableAbstractAggregateRoot::getId).orElse(0L);
    }
}