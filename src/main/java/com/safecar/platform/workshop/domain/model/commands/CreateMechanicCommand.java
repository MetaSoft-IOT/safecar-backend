package com.safecar.platform.workshop.domain.model.commands;

/**
 * Command to create a Mechanic in the Workshop BC.
 * <p>
 * This command contains workshop-specific information for a mechanic,
 * while referencing the PersonProfile from the Profiles BC.
 * </p>
 * 
 * @param profileId the ID of the PersonProfile from Profiles BC
 * @param companyName the name of the workshop company
 * @param specializations mechanic's technical specializations
 * @param yearsOfExperience years of professional experience
 */
public record CreateMechanicCommand(
        Long profileId,
        String companyName,
        String specializations,
        Integer yearsOfExperience
) {
}