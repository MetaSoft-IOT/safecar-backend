package com.safecar.platform.profiles.application.internal.commandservices;


import com.safecar.platform.profiles.domain.model.aggregates.Mechanic;
import com.safecar.platform.profiles.domain.model.commands.CreateMechanicCommand;
import com.safecar.platform.profiles.domain.services.MechanicCommandService;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.MechanicRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MechanicCommandServiceImpl implements MechanicCommandService {
    private final MechanicRepository mechanicRepository;

        public MechanicCommandServiceImpl(MechanicRepository mechanicRepository) {
        this.mechanicRepository = mechanicRepository;
    }

    @Override
    public Optional<Mechanic> handle(CreateMechanicCommand command, Long userId) {
        if (mechanicRepository.existsByPhone_Phone(command.phone())){
            throw new IllegalArgumentException("Phone already exists");
        }

        if (mechanicRepository.existsByDni(command.dni())){
            throw new IllegalArgumentException("Ruc already exists");
        }

        var mechanic = new Mechanic(command, userId);
        var createdMechanic = mechanicRepository.save(mechanic);
        return Optional.of(createdMechanic);
    }
}
