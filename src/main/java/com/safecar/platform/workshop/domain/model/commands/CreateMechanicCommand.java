package com.safecar.platform.workshop.domain.model.commands;

import java.util.Set;

import com.safecar.platform.workshop.domain.model.entities.Specialization;

/**
 * Command to create a Mechanic in the Workshop BC.
 * <p>
 * This command contains workshop-specific information for a mechanic,
 * while referencing the PersonProfile from the Profiles BC.
 * </p>
 * 
 * @param profileId the ID of the PersonProfile from Profiles BC
 * @param specializations set of specializations of the mechanic
 * @param yearsOfExperience years of professional experience
 */
public record CreateMechanicCommand(
        Long profileId,
        Set<Specialization> specializations,
        Integer yearsOfExperience
) {
}