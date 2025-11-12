package com.safecar.platform.workshop.application.internal.commandservices;

import org.springframework.stereotype.Service;
import com.safecar.platform.workshop.domain.model.aggregates.Mechanic;
import com.safecar.platform.workshop.domain.services.MechanicCommandService;
import com.safecar.platform.workshop.domain.model.commands.CreateMechanicCommand;
import com.safecar.platform.workshop.domain.model.commands.UpdateMechanicMetricsCommand;
import com.safecar.platform.workshop.domain.model.entities.Specialization;
import com.safecar.platform.workshop.domain.model.valueobjects.Specializations;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.MechanicRepository;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.SpecializationRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mechanic Command Service Implementation
 * <p>
 * This service handles commands related to mechanic management.
 * </p>
 */
@Service
public class MechanicCommandServiceImpl implements MechanicCommandService {

    private final MechanicRepository mechanicRepository;
    private final SpecializationRepository specializationRepository;

    public MechanicCommandServiceImpl(
            MechanicRepository mechanicRepository,
            SpecializationRepository specializationRepository) {
        this.mechanicRepository = mechanicRepository;
        this.specializationRepository = specializationRepository;
    }

    // inheritdoc javadoc
    @Override
    public Optional<Mechanic> handle(CreateMechanicCommand command) {

        Set<Specialization> specializations = Collections.emptySet();

        if (command.specializations() == null || command.specializations().isEmpty()) {
            var generalSpecialization = specializationRepository.findByName(Specializations.GENERAL)
                    .orElseThrow(() -> new RuntimeException("Default specialization 'General' not found."));
            specializations = Set.of(generalSpecialization);
        } else {
            specializations = command.specializations().stream()
                    .map(specialization -> specializationRepository.findByName(specialization.getName())
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "Specialization not found: " + specialization.getName())))
                    .collect(Collectors.toSet());
        }

    var mechanic = Mechanic.create(command);
    mechanic.addSpecializations(specializations); // ensure defaults applied if necessary
    var saved = mechanicRepository.save(mechanic);
    return Optional.of(saved);
    }

    // inheritdoc javadoc
    @Override
    public Optional<Mechanic> handle(UpdateMechanicMetricsCommand command, Long mechanicId) {

        var specializations = command.specializations().stream()
                .map(specialization -> specializationRepository.findByName(specialization.getName())
                        .orElseThrow(
                                () -> new RuntimeException("Specialization not found: " + specialization.getName())))
                .collect(Collectors.toSet());

        if (specializations.isEmpty())
            throw new IllegalArgumentException("Mechanic must have at least one specialization.");

        var mechanicOpt = mechanicRepository.findById(mechanicId);

        if (mechanicOpt.isEmpty())
            return Optional.empty();

        var mechanic = mechanicOpt.get();
        mechanic.updateSpecializations(specializations);
        mechanic.updateYearsOfExperience(command.yearsOfExperience());

        var updated = mechanicRepository.save(mechanic);
        return Optional.of(updated);
    }
}