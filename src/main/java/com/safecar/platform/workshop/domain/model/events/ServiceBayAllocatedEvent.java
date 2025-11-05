package com.safecar.platform.workshop.domain.model.events;

import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;

/**
 * Event fired when a service bay is allocated.
 */
public record ServiceBayAllocatedEvent(
        Long serviceBayId,
        WorkshopId workshopId,
        String label
) {
}
