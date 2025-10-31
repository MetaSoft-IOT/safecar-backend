package com.safecar.platform.profiles.domain.services;



import com.safecar.platform.profiles.domain.model.aggregates.Workshop;
import com.safecar.platform.profiles.domain.model.commands.CreateWorkshopCommand;

import java.util.Optional;

public interface WorkshopCommandService {
    Optional<Workshop> handle(CreateWorkshopCommand command, Long userId);
}
