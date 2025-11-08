package com.safecar.platform.devices.application.internal.commandservices;

import com.safecar.platform.devices.domain.model.aggregates.Driver;
import com.safecar.platform.devices.domain.model.commands.CreateDriverCommand;
import com.safecar.platform.devices.domain.model.commands.UpdateDriverMetricsCommand;
import com.safecar.platform.devices.domain.services.DriverCommandService;
import com.safecar.platform.devices.infrastructure.persistence.jpa.repositories.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DriverCommandServiceImpl implements DriverCommandService {
    private final DriverRepository driverRepository;

    public DriverCommandServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public Optional<Driver> handle(CreateDriverCommand command) {
        if (driverRepository.existsByProfileId_ProfileId(command.profileId()))
            throw new IllegalArgumentException("Driver already exists for profile ID: " + command.profileId());

        var driver = new Driver(command.profileId());
        var createdDriver = driverRepository.save(driver);
        return Optional.of(createdDriver);
    }

    @Override
    public Optional<Driver> handle(UpdateDriverMetricsCommand command, Long profileId) {

        if (!driverRepository.existsByProfileId_ProfileId(profileId))
            throw new IllegalArgumentException("Driver not found for profile ID: " + profileId);

        var driver = driverRepository.findByProfileId_ProfileId(profileId)
                .orElseThrow();

        driver.updateTotalVehicles();

        driverRepository.save(driver);
        return Optional.of(driver);
    }
}