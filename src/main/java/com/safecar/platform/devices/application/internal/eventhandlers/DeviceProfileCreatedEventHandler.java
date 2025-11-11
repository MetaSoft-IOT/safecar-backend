package com.safecar.platform.devices.application.internal.eventhandlers;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import com.safecar.platform.devices.domain.model.commands.CreateDriverCommand;
import com.safecar.platform.devices.domain.services.DriverCommandService;
import com.safecar.platform.shared.domain.model.events.ProfileCreatedEvent;

/**
 * Profile Created Event Handler for Devices BC
 * <p>
 * This event handler listens for {@link ProfileCreatedEvent} events and
 * automatically creates a basic Driver entity when the user has ROLE_DRIVER.
 * </p>
 */
@Component
public class DeviceProfileCreatedEventHandler {

    private final DriverCommandService commandService;

    /**
     * Constructor
     * 
     * @param commandService the {@link DriverCommandService} instance
     */
    public DeviceProfileCreatedEventHandler(DriverCommandService commandService) {
        this.commandService = commandService;
    }

    /**
     * Handles the {@link ProfileCreatedEvent} by creating a basic Driver if the user has ROLE_DRIVER.
     * The driver can be updated later through Devices BC endpoints.
     * 
     * @param event the {@link ProfileCreatedEvent} instance
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(ProfileCreatedEvent event) {
        var isDriver = event.userRoles().contains("ROLE_DRIVER");

        if (isDriver) {
            var command = new CreateDriverCommand(
                    event.profileId());

            commandService.handle(command);
        }
    }
}