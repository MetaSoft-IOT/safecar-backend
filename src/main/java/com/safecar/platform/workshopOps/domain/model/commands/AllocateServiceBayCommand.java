package com.safecar.platform.workshopOps.domain.model.commands;

import com.safecar.platform.workshopOps.domain.model.valueobjects.WorkshopId;

/**
 * Command to allocate a service bay in a workshop.
 */
public record AllocateServiceBayCommand(
        WorkshopId workshopId,
        String label
) {
}
