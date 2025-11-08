package com.safecar.platform.devices.domain.services;

import com.safecar.platform.devices.domain.model.aggregates.Driver;
import com.safecar.platform.devices.domain.model.commands.CreateDriverCommand;
import com.safecar.platform.devices.domain.model.commands.UpdateDriverMetricsCommand;

import java.util.Optional;

/**
 * Driver Command Service Interface
 * <p>
 * This interface defines the contract for handling driver-related commands
 * within the Devices bounded context.
 * </p>
 */
public interface DriverCommandService {
    /**
     * Handles the creation of a new driver based on the provided command.
     * 
     * @param command the command containing driver creation details
     * @return an Optional containing the created Driver, or empty if creation
     *         failed
     */
    Optional<Driver> handle(CreateDriverCommand command);

    /**
     * Handles the update of driver metrics based on the provided command.
     * 
     * @param command   the command containing driver metrics update details
     * @param profileId the profile ID of the driver to be updated
     * @return an Optional containing the updated Driver, or empty if update failed
     */
    Optional<Driver> handle(UpdateDriverMetricsCommand command, Long profileId);
}