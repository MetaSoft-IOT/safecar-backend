package com.safecar.platform.profiles.domain.services;

import com.safecar.platform.profiles.domain.model.aggregates.WorkshopMechanic;
import com.safecar.platform.profiles.domain.model.commands.CreateWorkshopMechanicCommand;

import java.util.Optional;

/**
 * Workshop Mechanic Command Service
 * <p>
 * Handles commands related to Workshop Mechanic operations.
 * </p>
 */
public interface WorkshopMechanicCommandService {
    /**
     * Handles the creation of a new Workshop Mechanic.
     * 
     * @param command the command containing workshop mechanic creation details
     * @param userId  the ID of the user associated with the workshop mechanic
     * @return an Optional containing the created Workshop Mechanic, or empty if
     *         creation failed
     */
    Optional<WorkshopMechanic> handle(CreateWorkshopMechanicCommand command, Long userId);
}
