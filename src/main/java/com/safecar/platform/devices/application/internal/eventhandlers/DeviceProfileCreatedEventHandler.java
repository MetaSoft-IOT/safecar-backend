package com.safecar.platform.devices.application.internal.eventhandlers;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.safecar.platform.devices.domain.model.commands.CreateDriverCommand;
import com.safecar.platform.devices.domain.services.DriverCommandService;
import com.safecar.platform.shared.domain.model.events.PersonProfileCreatedEvent;

/**
 * Person Profile Created Event Handler for Devices BC
 * <p>
 * This event handler listens for {@link PersonProfileCreatedEvent} events and
 * automatically creates a basic Driver entity.
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
     * Handles the {@link PersonProfileCreatedEvent} by creating a basic Driver.
     * The driver can be updated later through Devices BC endpoints.
     * 
     * @param event the {@link PersonProfileCreatedEvent} instance
     */
    @EventListener
    public void on(PersonProfileCreatedEvent event) {
        var isDriver = event.userRoles().contains("ROLE_DRIVER");

        if (isDriver) {
            var command = new CreateDriverCommand(
                    event.profileId());

            commandService.handle(command);
        }
    }
}