package com.safecar.platform.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * RUC (Registro Único de Contribuyentes) Value Object.
 * <p>
 * Represents a unique tax identification number in Peru, consisting of exactly
 * 11 digits.
 * </p>
 */
@Embeddable
public record Ruc(String ruc) {
    public Ruc {
        if (ruc == null || !ruc.matches("\\d{11}")) {
            throw new IllegalArgumentException("Invalid RUC: must be a positive number with exactly 11 digits");
        }
    }

    public Ruc() {
        this("00000000000"); // Valor por defecto opcional
    }
}
