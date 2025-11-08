package com.safecar.platform.workshop.application.internal.eventhandlers;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.safecar.platform.shared.domain.model.events.PersonProfileCreatedEvent;
import com.safecar.platform.workshop.domain.model.commands.CreateMechanicCommand;
import com.safecar.platform.workshop.domain.services.MechanicCommandService;

/**
 * Mechanic Profile Created Event Handler for Workshop BC
 * <p>
 * This event handler listens for {@link PersonProfileCreatedEvent} events and
 * automatically creates a basic Mechanic entity.
 * </p>
 */
@Component
public class MechanicProfileCreatedEventHandler {

    private final MechanicCommandService commandService;

    /**
     * Constructor
     * 
     * @param commandService the {@link MechanicCommandService} instance
     */
    public MechanicProfileCreatedEventHandler(MechanicCommandService commandService) {
        this.commandService = commandService;
    }

    /**
     * Handles the {@link PersonProfileCreatedEvent} by creating a basic Mechanic
     * with default values. The mechanic can be updated later through Workshop BC
     * endpoints.
     * 
     * @param event the {@link PersonProfileCreatedEvent} instance
     */
    @EventListener
    public void on(PersonProfileCreatedEvent event) {

        var isMechanic = event.userRoles().contains("ROLE_MECHANIC");

        if (isMechanic) {
            var command = new CreateMechanicCommand(
                    event.profileId(),
                    "Default Company",
                    null,
                    0);

            commandService.handle(command);
        }
    }
}