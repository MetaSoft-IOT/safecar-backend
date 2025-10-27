package com.safecar.platform.profiles.domain.services;



import com.safecar.platform.profiles.domain.model.aggregates.Mechanic;
import com.safecar.platform.profiles.domain.model.commands.CreateMechanicCommand;

import java.util.Optional;
import java.util.UUID;

public interface MechanicCommandService {
    Optional<Mechanic> handle(CreateMechanicCommand command, UUID userId);
}
