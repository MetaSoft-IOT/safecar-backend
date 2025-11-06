package com.safecar.platform.shared.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * ProfileId value object
 * <p>
 * Represents a reference to a PersonProfile from the Profiles BC.
 * This enables other BCs to reference person information without tight coupling.
 * </p>
 */
@Embeddable
public record ProfileId(@NotNull @Positive Long profileId) {
    
    /**
     * Default constructor for JPA
     */
    public ProfileId() {
        this(0L);
    }
}