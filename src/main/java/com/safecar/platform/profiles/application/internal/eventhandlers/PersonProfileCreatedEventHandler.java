package com.safecar.platform.profiles.application.internal.eventhandlers;

import java.util.logging.Logger;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.safecar.platform.shared.domain.model.events.PersonProfileCreatedEvent;

@Component("profilesPersonProfileCreatedEventHandler")
public class PersonProfileCreatedEventHandler {

    private final Logger logger = Logger.getLogger(PersonProfileCreatedEvent.class.getName());

    @EventListener
    public void onPersonProfileCreated(PersonProfileCreatedEvent event) {
        logger.info("Person profile created: " + event.profileId());
    }   
}
