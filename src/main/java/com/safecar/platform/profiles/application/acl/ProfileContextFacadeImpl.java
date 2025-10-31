package com.safecar.platform.profiles.application.acl;


import com.safecar.platform.profiles.domain.model.commands.CreateDriverCommand;
import com.safecar.platform.profiles.domain.model.commands.CreateMechanicCommand;
import com.safecar.platform.profiles.domain.model.queries.GetDriverByUserIdAsyncQuery;
import com.safecar.platform.profiles.domain.model.queries.GetMechanicByUserIdAsyncQuery;
import com.safecar.platform.profiles.domain.services.DriverCommandService;
import com.safecar.platform.profiles.domain.services.DriverQueryService;
import com.safecar.platform.profiles.domain.services.MechanicCommandService;
import com.safecar.platform.profiles.domain.services.MechanicQueryService;
import com.safecar.platform.profiles.interfaces.acl.ProfilesContextFacade;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileContextFacadeImpl implements ProfilesContextFacade {
    private final DriverCommandService driverCommandService;
    private final DriverQueryService driverQueryService;
    private final MechanicCommandService mechanicCommandService;
    private final MechanicQueryService mechanicQueryService;

    public ProfileContextFacadeImpl(DriverCommandService driverCommandService, DriverQueryService driverQueryService,
                                    MechanicCommandService mechanicCommandService, MechanicQueryService mechanicQueryService) {
        this.driverCommandService = driverCommandService;
        this.driverQueryService = driverQueryService;
        this.mechanicCommandService = mechanicCommandService;
        this.mechanicQueryService = mechanicQueryService;
    }

    @Override
    public Long createDriver(String fullName, String city, String country,
                             String phone, String dni, Long userId) {

        CreateDriverCommand command = new CreateDriverCommand(
                fullName, city, country, phone, dni);

        var driver = driverCommandService.handle(command, userId);
        return driver.map(AuditableAbstractAggregateRoot::getId) .orElse( 0L);
    }

    @Override
    public Long createWorkshop(String fullName, String city, String country,
                               String phone, String companyName, String dni, Long userId) {
        CreateMechanicCommand command = new CreateMechanicCommand(
                fullName, city, country, phone, companyName, dni);

        var mechanic = mechanicCommandService.handle(command, userId);
        return mechanic.map(AuditableAbstractAggregateRoot::getId).orElse(0L);
    }

    @Override
    public boolean exitsDriverByUserId(Long userId) {
        var query = new GetDriverByUserIdAsyncQuery(userId);
        var existingMechanic = driverQueryService.handle(query);
        return existingMechanic.isPresent();
    }

    @Override
    public boolean exitsMechanicByUserId(Long userId) {
        var query = new GetMechanicByUserIdAsyncQuery(userId);
        var existingMechanic = mechanicQueryService.handle(query);
        return existingMechanic.isPresent();
    };
}