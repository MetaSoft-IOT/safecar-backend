package com.safecar.platform.workshop.application.acl;

import java.util.List;
import java.util.stream.Collectors;

import com.safecar.platform.workshop.domain.model.commands.CreateMechanicCommand;
import com.safecar.platform.workshop.domain.model.entities.Specialization;
import com.safecar.platform.workshop.domain.model.queries.GetMechanicByProfileIdQuery;
import com.safecar.platform.workshop.domain.services.MechanicCommandService;
import com.safecar.platform.workshop.domain.services.MechanicQueryService;
import com.safecar.platform.workshop.interfaces.acl.WorkshopContextFacade;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import org.springframework.stereotype.Service;

/**
 * Implementation of Workshop Context Facade
 * <p>
 * The implemplementation of {@link WorkshopContextFacade} that provides methods
 * to interact with the workshop context.
 * </p>
 */
@Service
public class WorkshopContextFacadeImpl implements WorkshopContextFacade {
    /**
     * Mechanic command and query services
     */
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
    public Long createMechanic(Long profileId, String companyName, List<String> specializationNames,
            Integer yearsOfExperience) {

        var specializations = specializationNames.stream().map(Specialization::toSpecializationFromName)
                .collect(Collectors.toSet());
        var createMechanicCommand = new CreateMechanicCommand(profileId, companyName, specializations,
                yearsOfExperience);
        var result = mechanicCommandService.handle(createMechanicCommand);

        if (result.isEmpty())
            return 0L;

        return result.get().getId();
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