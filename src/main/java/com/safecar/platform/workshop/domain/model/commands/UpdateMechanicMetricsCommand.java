package com.safecar.platform.workshop.domain.model.commands;

import java.util.Set;

import com.safecar.platform.workshop.domain.model.entities.Specialization;

/**
 * Update Mechanic Command
 * <p>
 *  This command contains updated workshop-specific information for a mechanic.
 * </p>
 * @param companyName the name of the workshop company
 * @param specializations set of specializations of the mechanic
 * @param yearsOfExperience years of professional experience
 */
public record UpdateMechanicMetricsCommand(
        String companyName,
        Set<Specialization> specializations,
        Integer yearsOfExperience) {
}
