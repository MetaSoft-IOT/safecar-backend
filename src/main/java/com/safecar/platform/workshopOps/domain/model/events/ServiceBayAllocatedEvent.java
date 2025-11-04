package com.safecar.platform.workshopOps.domain.model.events;

import com.safecar.platform.workshopOps.domain.model.valueobjects.WorkshopId;

/**
 * Event fired when a service bay is allocated.
 */
public record ServiceBayAllocatedEvent(
        Long serviceBayId,
        WorkshopId workshopId,
        String label
) {
}
