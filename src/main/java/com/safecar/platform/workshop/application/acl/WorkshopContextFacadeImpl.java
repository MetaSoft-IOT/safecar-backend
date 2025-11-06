package com.safecar.platform.workshop.application.acl;

import com.safecar.platform.workshop.domain.model.commands.CreateMechanicCommand;
import com.safecar.platform.workshop.domain.model.queries.GetMechanicByProfileIdQuery;
import com.safecar.platform.workshop.domain.services.MechanicCommandService;
import com.safecar.platform.workshop.domain.services.MechanicQueryService;
import com.safecar.platform.workshop.interfaces.acl.WorkshopContextFacade;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import org.springframework.stereotype.Service;

/**
 * Implementation of the Workshop Context Facade (Anti-Corruption Layer).
 * 
 * This service provides a stable interface for external bounded contexts
 * to interact with the Workshop domain, ensuring loose coupling and
 * protecting internal domain model changes.
 */
@Service
public class WorkshopContextFacadeImpl implements WorkshopContextFacade {
    private final MechanicCommandService mechanicCommandService;
    private final MechanicQueryService mechanicQueryService;

    /**
     * Constructor for WorkshopContextFacadeImpl.
     *
     * @param mechanicCommandService mechanic command service
     * @param mechanicQueryService   mechanic query service
     */
    public WorkshopContextFacadeImpl(MechanicCommandService mechanicCommandService, 
                                     MechanicQueryService mechanicQueryService) {
        this.mechanicCommandService = mechanicCommandService;
        this.mechanicQueryService = mechanicQueryService;
    }

    // inherited javadoc
    @Override
    public Long createMechanic(Long profileId, String companyName, String specializations, Integer yearsOfExperience) {
        var command = new CreateMechanicCommand(profileId, companyName, specializations, yearsOfExperience);
        var mechanic = mechanicCommandService.handle(command);
        return mechanic.map(AuditableAbstractAggregateRoot::getId).orElse(0L);
    }

    // inherited javadoc
    @Override
    public boolean existsMechanicByProfileId(Long profileId) {
        var query = new GetMechanicByProfileIdQuery(profileId);
        var existingMechanic = mechanicQueryService.handle(query);
        return existingMechanic.isPresent();
    }

    // inherited javadoc
    @Override
    public Long getMechanicIdByProfileId(Long profileId) {
        var query = new GetMechanicByProfileIdQuery(profileId);
        var mechanic = mechanicQueryService.handle(query);
        return mechanic.map(AuditableAbstractAggregateRoot::getId).orElse(0L);
    }
}