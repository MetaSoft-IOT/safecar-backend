package com.safecar.platform.workshop.application.internal.eventhandlers;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import com.safecar.platform.shared.domain.model.events.ProfileCreatedEvent;
import com.safecar.platform.workshop.domain.model.commands.CreateMechanicCommand;
import com.safecar.platform.workshop.domain.services.MechanicCommandService;

/**
 * Mechanic Profile Created Event Handler for Workshop BC
 * <p>
 * This event handler listens for {@link ProfileCreatedEvent} events and
 * automatically creates a basic Mechanic entity when the user has
 * ROLE_MECHANIC.
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
     * Handles the {@link ProfileCreatedEvent} by creating a basic Mechanic
     * with default values when the user has ROLE_MECHANIC. The mechanic can be
     * updated later through Workshop BC endpoints.
     * 
     * @param event the {@link ProfileCreatedEvent} instance
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void on(ProfileCreatedEvent event) {
        var isMechanic = event.userRoles().contains("ROLE_MECHANIC");

        if (isMechanic) {
            var command = new CreateMechanicCommand(
                    event.profileId(),
                    "Independent Mechanic", // Default company name that makes sense
                    null,
                    0);

            commandService.handle(command);
        }
    }
}