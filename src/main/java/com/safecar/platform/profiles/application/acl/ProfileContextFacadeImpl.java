package com.safecar.platform.profiles.application.acl;

import com.safecar.platform.profiles.domain.model.commands.CreatePersonProfileCommand;
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
    private final PersonProfileCommandService personProfileCommandService;
    private final PersonProfileQueryService personProfileQueryService;

    /**
     * Constructor for ProfileContextFacadeImpl.
     *
     * @param driverCommandService           driver command service
     * @param driverQueryService             driver query service
     * @param workshopMechanicCommandService workshop mechanic command service
     * @param workshopMechanicQueryService   workshop mechanic query service
     */
    public ProfileContextFacadeImpl(PersonProfileCommandService personProfileCommandService,
                                    PersonProfileQueryService personProfileQueryService) {
        this.personProfileCommandService = personProfileCommandService;
        this.personProfileQueryService = personProfileQueryService;
    }

    // inherited javadoc
    @Override
    public Long createPersonProfile(String fullName, String city, String country,
                                    String phone, String dni, Long userId) {
        var command = new CreatePersonProfileCommand(fullName, city, country, phone, dni);
        var created = personProfileCommandService.handle(command, userId);
        return created.map(AuditableAbstractAggregateRoot::getId).orElse(0L);
    }

    @Override
    public boolean existsPersonProfileByUserId(Long userId) {
        var existing = personProfileQueryService.findByUserId(userId);
        return existing.isPresent();
    }

    @Override
    public boolean existsPersonProfileById(Long profileId) {
        var existing = personProfileQueryService.findById(profileId);
        return existing.isPresent();
    }

    @Override
    public Long getPersonProfileIdByUserId(Long userId) {
        var person = personProfileQueryService.findByUserId(userId);
        return person.map(AuditableAbstractAggregateRoot::getId).orElse(0L);
    }
}