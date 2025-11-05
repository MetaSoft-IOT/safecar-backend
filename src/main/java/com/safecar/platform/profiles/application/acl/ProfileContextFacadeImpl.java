package com.safecar.platform.profiles.application.acl;

import com.safecar.platform.profiles.domain.model.commands.CreateDriverCommand;
import com.safecar.platform.profiles.domain.model.commands.CreateWorkshopMechanicCommand;
import com.safecar.platform.profiles.domain.model.queries.GetDriverByUserIdQuery;
import com.safecar.platform.profiles.domain.model.queries.GetWorkshopMechanicByUserIdQuery;
import com.safecar.platform.profiles.domain.services.DriverCommandService;
import com.safecar.platform.profiles.domain.services.DriverQueryService;
import com.safecar.platform.profiles.domain.services.WorkshopMechanicCommandService;
import com.safecar.platform.profiles.domain.services.WorkshopMechanicQueryService;
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
    private final DriverCommandService driverCommandService;
    private final DriverQueryService driverQueryService;
    private final WorkshopMechanicCommandService workshopMechanicCommandService;
    private final WorkshopMechanicQueryService workshopMechanicQueryService;

    /**
     * Constructor for ProfileContextFacadeImpl.
     *
     * @param driverCommandService           driver command service
     * @param driverQueryService             driver query service
     * @param workshopMechanicCommandService workshop mechanic command service
     * @param workshopMechanicQueryService   workshop mechanic query service
     */
    public ProfileContextFacadeImpl(DriverCommandService driverCommandService, DriverQueryService driverQueryService,
            WorkshopMechanicCommandService workshopMechanicCommandService,
            WorkshopMechanicQueryService workshopMechanicQueryService) {
        this.driverCommandService = driverCommandService;
        this.driverQueryService = driverQueryService;
        this.workshopMechanicCommandService = workshopMechanicCommandService;
        this.workshopMechanicQueryService = workshopMechanicQueryService;
    }

    // inherited javadoc
    @Override
    public Long createDriver(String fullName, String city, String country,
            String phone, String dni, Long userId) {

        CreateDriverCommand command = new CreateDriverCommand(
                fullName, city, country, phone, dni);

        var driver = driverCommandService.handle(command, userId);
        return driver.map(AuditableAbstractAggregateRoot::getId).orElse(0L);
    }

    // inherited javadoc
    @Override
    public Long createWorkshopMechanic(String fullName, String city, String country,
            String phone, String companyName, String dni, Long userId) {
        CreateWorkshopMechanicCommand command = new CreateWorkshopMechanicCommand(
                fullName, city, country, phone, companyName, dni);

        var workshopMechanic = workshopMechanicCommandService.handle(command, userId);
        return workshopMechanic.map(AuditableAbstractAggregateRoot::getId).orElse(0L);
    }

    // inherited javadoc
    @Override
    public boolean existsDriverByUserId(Long userId) {
        var query = new GetDriverByUserIdQuery(userId);
        var existingDriver = driverQueryService.handle(query);
        return existingDriver.isPresent();
    }

    // inherited javadoc
    @Override
    public boolean existsWorkshopMechanicByUserId(Long userId) {
        var query = new GetWorkshopMechanicByUserIdQuery(userId);
        var existingWorkshopMechanic = workshopMechanicQueryService.handle(query);
        return existingWorkshopMechanic.isPresent();
    }

    // inherited javadoc
    @Override
    public Long getDriverIdByUserId(Long userId) {
        var query = new GetDriverByUserIdQuery(userId);
        var driver = driverQueryService.handle(query);
        return driver.map(AuditableAbstractAggregateRoot::getId).orElse(0L);
    }

    // inherited javadoc
    @Override
    public Long getWorkshopMechanicIdByUserId(Long userId) {
        var query = new GetWorkshopMechanicByUserIdQuery(userId);
        var workshopMechanic = workshopMechanicQueryService.handle(query);
        return workshopMechanic.map(AuditableAbstractAggregateRoot::getId).orElse(0L);
    }
}