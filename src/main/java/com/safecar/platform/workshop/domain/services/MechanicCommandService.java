package com.safecar.platform.workshop.domain.services;

import com.safecar.platform.workshop.domain.model.aggregates.Mechanic;
import com.safecar.platform.workshop.domain.model.commands.CreateMechanicCommand;

import java.util.Optional;

/**
 * Mechanic Command Service Interface
 * <p>
 * This interface defines the contract for handling mechanic-related commands
 * within the Workshop bounded context.
 * </p>
 */
public interface MechanicCommandService {
    /**
     * Handles the creation of a new mechanic based on the provided command.
     * 
     * @param command the command containing mechanic creation details
     * @return an Optional containing the created Mechanic, or empty if creation failed
     */
    Optional<Mechanic> handle(CreateMechanicCommand command);
}