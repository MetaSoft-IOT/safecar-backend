package com.safecar.platform.profiles.application.internal.commandservices;


import com.safecar.platform.profiles.domain.model.aggregates.Driver;
import com.safecar.platform.profiles.domain.model.commands.CreateDriverCommand;
import com.safecar.platform.profiles.domain.services.DriverCommandService;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DriverCommandServiceImpl implements DriverCommandService {
    private final DriverRepository driverRepository;

    public DriverCommandServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public Optional<Driver> handle(CreateDriverCommand command, Long userId) {

        if (driverRepository.existsByPhone_Phone(command.phone())) {
            throw new IllegalArgumentException("Phone already exists");
        }

        if (driverRepository.existsByDni_Dni(command.dni())) {
            throw new IllegalArgumentException("DNI already exists");
        }
        var driver = new Driver(command, userId);
        var createdDriver = driverRepository.save(driver);
        return Optional.of(createdDriver);
    }
}
