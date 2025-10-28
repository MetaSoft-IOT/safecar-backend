package com.safecar.platform.profiles.interfaces.rest;

import com.safecar.platform.profiles.domain.model.queries.GetDriverByUserIdAsyncQuery;
import com.safecar.platform.profiles.domain.model.queries.GetMechanicByUserIdAsyncQuery;
import com.safecar.platform.profiles.domain.services.DriverQueryService;
import com.safecar.platform.profiles.domain.services.MechanicQueryService;
import com.safecar.platform.profiles.interfaces.rest.resource.DriverResource;
import com.safecar.platform.profiles.interfaces.rest.resource.MechanicResource;
import com.safecar.platform.profiles.interfaces.rest.transform.DriverResourceFromEntityAssembler;
import com.safecar.platform.profiles.interfaces.rest.transform.MechanicResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "Profiles Management Endpoints")
public class ProfileController {
    private final DriverQueryService driverQueryService;
    private final MechanicQueryService mechanicQueryService;

    public ProfileController(DriverQueryService driverQueryService,
                             MechanicQueryService mechanicQueryService) {
        this.driverQueryService = driverQueryService;
        this.mechanicQueryService = mechanicQueryService;
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

    @GetMapping(value = "/vehicle/{userId}")
    public ResponseEntity<MechanicResource> getMechanic(@PathVariable Long userId) {
        var getMechanicByUserIdQuery = new GetMechanicByUserIdAsyncQuery(userId);
        var mechanic = mechanicQueryService.handle(getMechanicByUserIdQuery);
        if (mechanic.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var mechanicResource = MechanicResourceFromEntityAssembler.toResourceFromEntity(mechanic.get());

        return ResponseEntity.ok(mechanicResource);
    }
}
