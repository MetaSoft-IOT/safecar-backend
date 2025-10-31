package com.safecar.platform.profiles.domain.services;



import com.safecar.platform.profiles.domain.model.aggregates.Driver;
import com.safecar.platform.profiles.domain.model.commands.CreateDriverCommand;

import java.util.Optional;

public interface DriverCommandService {
    Optional<Driver> handle(CreateDriverCommand command, Long userId);
}
