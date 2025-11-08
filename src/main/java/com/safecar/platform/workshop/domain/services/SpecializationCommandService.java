package com.safecar.platform.workshop.domain.services;

import com.safecar.platform.workshop.domain.model.commands.SeedSpecializationsCommand;

/**
 * Service interface for Specialization command operations.
 * <p>
 * This interface defines the contract for handling specialization-related commands.
 * </p>
 * 
 * @author SafeCar Platform Team
 * @version 1.0
 * @since 2025-11-07
 */
public interface SpecializationCommandService {

    /**
     * Handles the seed specializations command.
     * <p>
     * This method processes the command to seed default specializations into the system.
     * </p>
     *
     * @param command the {@link SeedSpecializationsCommand} to handle
     */
    void handle(SeedSpecializationsCommand command);
}