package com.safecar.platform.workshop.domain.services;

import com.safecar.platform.workshop.domain.model.aggregates.Mechanic;
import com.safecar.platform.workshop.domain.model.commands.CreateMechanicCommand;
import com.safecar.platform.workshop.domain.model.commands.UpdateMechanicMetricsCommand;

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
     * @return an Optional containing the created Mechanic, or empty if creation
     *         failed
     */
    Optional<Mechanic> handle(CreateMechanicCommand command);

    /**
     * Handles the update of an existing mechanic based on the provided command.
     * 
     * @param command   the command containing mechanic update details
     * @param profileId the ID of the mechanic profile to update
     * @return an Optional containing the updated Mechanic, or empty if update failed
     */
    Optional<Mechanic> handle(UpdateMechanicMetricsCommand command, Long profileId);
}