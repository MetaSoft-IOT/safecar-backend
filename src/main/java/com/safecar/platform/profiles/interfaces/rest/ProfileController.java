package com.safecar.platform.profiles.interfaces.rest;

import com.safecar.platform.profiles.domain.model.queries.GetDriverByUserIdAsyncQuery;
import com.safecar.platform.profiles.domain.model.queries.GetWorkshopByUserIdAsyncQuery;
import com.safecar.platform.profiles.domain.services.DriverQueryService;
import com.safecar.platform.profiles.domain.services.WorkshopQueryService;
import com.safecar.platform.profiles.interfaces.rest.resource.DriverResource;
import com.safecar.platform.profiles.interfaces.rest.resource.WorkshopResource;
import com.safecar.platform.profiles.interfaces.rest.transform.DriverResourceFromEntityAssembler;
import com.safecar.platform.profiles.interfaces.rest.transform.WorkshopResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "Profiles Management Endpoints")
public class ProfileController {
    private final DriverQueryService driverQueryService;
    private final WorkshopQueryService workshopQueryService;

    public ProfileController(DriverQueryService driverQueryService,
                             WorkshopQueryService workshopQueryService) {
        this.driverQueryService = driverQueryService;
        this.workshopQueryService = workshopQueryService;
    }

    @GetMapping(value = "/driver/{userId}")
    public ResponseEntity<DriverResource> getDriver(@PathVariable Long userId) {
        var getDriverByUserIdQuery = new GetDriverByUserIdAsyncQuery(userId);
        var driver = driverQueryService.handle(getDriverByUserIdQuery);
        if (driver.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var driverResource = DriverResourceFromEntityAssembler
                .toResourceFromEntity(driver.get());

        return ResponseEntity.ok(driverResource);
    }

    @GetMapping(value = "/workshop/{userId}")
    public ResponseEntity<WorkshopResource> getWorkshop(@PathVariable Long userId) {
        var getWorkshopByUserIdQuery = new GetWorkshopByUserIdAsyncQuery(userId);
        var workshop = workshopQueryService.handle(getWorkshopByUserIdQuery);
        if (workshop.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var workshopResource = WorkshopResourceFromEntityAssembler.toResourceFromEntity(workshop.get());

        return ResponseEntity.ok(workshopResource);
    }
}
