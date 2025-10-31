package com.safecar.platform.profiles.application.acl;


import com.safecar.platform.profiles.domain.model.commands.CreateDriverCommand;
import com.safecar.platform.profiles.domain.model.commands.CreateWorkshopCommand;
import com.safecar.platform.profiles.domain.model.queries.GetDriverByUserIdAsyncQuery;
import com.safecar.platform.profiles.domain.model.queries.GetWorkshopByUserIdAsyncQuery;
import com.safecar.platform.profiles.domain.services.DriverCommandService;
import com.safecar.platform.profiles.domain.services.DriverQueryService;
import com.safecar.platform.profiles.domain.services.WorkshopCommandService;
import com.safecar.platform.profiles.domain.services.WorkshopQueryService;
import com.safecar.platform.profiles.interfaces.acl.ProfilesContextFacade;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileContextFacadeImpl implements ProfilesContextFacade {
    private final DriverCommandService driverCommandService;
    private final DriverQueryService driverQueryService;
    private final WorkshopCommandService workshopCommandService;
    private final WorkshopQueryService workshopQueryService;

    public ProfileContextFacadeImpl(DriverCommandService driverCommandService, DriverQueryService driverQueryService,
                                    WorkshopCommandService workshopCommandService, WorkshopQueryService workshopQueryService) {
        this.driverCommandService = driverCommandService;
        this.driverQueryService = driverQueryService;
        this.workshopCommandService = workshopCommandService;
        this.workshopQueryService = workshopQueryService;
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
        CreateWorkshopCommand command = new CreateWorkshopCommand(
                fullName, city, country, phone, companyName, dni);

        var workshop = workshopCommandService.handle(command, userId);
        return workshop.map(AuditableAbstractAggregateRoot::getId).orElse(0L);
    }

    @Override
    public boolean exitsDriverByUserId(Long userId) {
        var query = new GetDriverByUserIdAsyncQuery(userId);
        var existingDriver = driverQueryService.handle(query);
        return existingDriver.isPresent();
    }

    @Override
    public boolean exitsWorkshopByUserId(Long userId) {
        var query = new GetWorkshopByUserIdAsyncQuery(userId);
        var existingWorkshop = workshopQueryService.handle(query);
        return existingWorkshop.isPresent();
    }
}