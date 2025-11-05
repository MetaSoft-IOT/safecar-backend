package com.safecar.platform.profiles.domain.services;

import com.safecar.platform.profiles.domain.model.aggregates.Driver;
import com.safecar.platform.profiles.domain.model.commands.CreateDriverCommand;

import java.util.Optional;

/**
 * Driver Command Service Interface
 * <p>
 * This interface defines the contract for handling driver-related commands.
 * It includes methods for creating and managing driver entities within the
 * system.
 * </p>
 */
public interface DriverCommandService {
    /**
     * Handles the creation of a new driver based on the provided command and user
     * ID.
     * 
     * @param command the command containing driver creation details
     * @param userId  the ID of the user associated with the driver
     * @return an Optional containing the created Driver, or empty if creation
     *         failed
     */
    Optional<Driver> handle(CreateDriverCommand command, Long userId);
}
