package com.safecar.platform.profiles.application.internal.commandservices;


import com.safecar.platform.profiles.domain.model.aggregates.Workshop;
import com.safecar.platform.profiles.domain.model.commands.CreateWorkshopCommand;
import com.safecar.platform.profiles.domain.services.WorkshopCommandService;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.WorkshopRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorkshopCommandServiceImpl implements WorkshopCommandService {
    private final WorkshopRepository workshopRepository;

    public WorkshopCommandServiceImpl(WorkshopRepository workshopRepository) {
        this.workshopRepository = workshopRepository;
    }

    @Override
    public Optional<Workshop> handle(CreateWorkshopCommand command, Long userId) {
        if (workshopRepository.existsByPhone_Phone(command.phone())) {
            throw new IllegalArgumentException("Phone already exists");
        }

        if (workshopRepository.existsByDni(command.dni())) {
            throw new IllegalArgumentException("Ruc already exists");
        }

        var workshop = new Workshop(command, userId);
        var createdWorkshop = workshopRepository.save(workshop);
        return Optional.of(createdWorkshop);
    }
}
