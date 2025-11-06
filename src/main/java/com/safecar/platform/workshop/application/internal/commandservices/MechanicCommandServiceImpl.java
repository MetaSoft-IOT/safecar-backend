package com.safecar.platform.workshop.application.internal.commandservices;

import com.safecar.platform.workshop.domain.model.aggregates.Mechanic;
import com.safecar.platform.workshop.domain.model.commands.CreateMechanicCommand;
import com.safecar.platform.workshop.domain.services.MechanicCommandService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.MechanicRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MechanicCommandServiceImpl implements MechanicCommandService {
    private final MechanicRepository mechanicRepository;

    public MechanicCommandServiceImpl(MechanicRepository mechanicRepository) {
        this.mechanicRepository = mechanicRepository;
    }

    @Override
    public Optional<Mechanic> handle(CreateMechanicCommand command) {
        // Validate that mechanic doesn't already exist for this profile
        if (mechanicRepository.existsByProfileId_ProfileId(command.profileId())) {
            throw new IllegalArgumentException("Mechanic already exists for profile ID: " + command.profileId());
        }

        var mechanic = new Mechanic(command);
        var createdMechanic = mechanicRepository.save(mechanic);
        return Optional.of(createdMechanic);
    }
}