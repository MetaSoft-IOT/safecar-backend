package com.safecar.platform.workshop.domain.model.commands;

import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;

/**
 * Command to allocate a service bay in a workshop.
 */
public record AllocateServiceBayCommand(
        WorkshopId workshopId,
        String label
) {
}
