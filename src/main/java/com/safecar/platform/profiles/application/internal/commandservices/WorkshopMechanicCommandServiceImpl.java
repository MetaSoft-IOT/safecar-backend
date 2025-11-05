package com.safecar.platform.profiles.application.internal.commandservices;

import com.safecar.platform.profiles.domain.model.aggregates.WorkshopMechanic;
import com.safecar.platform.profiles.domain.model.commands.CreateWorkshopMechanicCommand;
import com.safecar.platform.profiles.domain.services.WorkshopMechanicCommandService;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.WorkshopMechanicRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorkshopMechanicCommandServiceImpl implements WorkshopMechanicCommandService {
    private final WorkshopMechanicRepository workshopMechanicRepository;

    public WorkshopMechanicCommandServiceImpl(WorkshopMechanicRepository workshopMechanicRepository) {
        this.workshopMechanicRepository = workshopMechanicRepository;
    }

    @Override
    public Optional<WorkshopMechanic> handle(CreateWorkshopMechanicCommand command, Long userId) {
        if (workshopMechanicRepository.existsByPhone_Phone(command.phone())) {
            throw new IllegalArgumentException("Phone already exists");
        }

        if (workshopMechanicRepository.existsByDni(command.dni())) {
            throw new IllegalArgumentException("Tax ID already exists");
        }

        var workshopMechanic = new WorkshopMechanic(command, userId);
        var createdWorkshopMechanic = workshopMechanicRepository.save(workshopMechanic);
        return Optional.of(createdWorkshopMechanic);
    }
}
