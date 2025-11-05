package com.safecar.platform.workshopOps.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object representing a Workshop identifier with associated display name.
 */
@Embeddable
public record WorkshopId(
        @Column(name = "workshop_id", nullable = false)
        Long workshopId,
        
        @Column(name = "workshop_display_name", nullable = false, length = 200)
        String displayName
) {
    public WorkshopId {
        if (workshopId == null || workshopId <= 0) {
            throw new IllegalArgumentException("Workshop ID must be a positive value");
        }
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("Display name cannot be null or empty");
        }
    }
}