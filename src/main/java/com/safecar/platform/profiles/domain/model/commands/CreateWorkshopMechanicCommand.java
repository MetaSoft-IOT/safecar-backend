package com.safecar.platform.profiles.domain.model.commands;

/**
 * Create Workshop Mechanic Command
 * <p>
 * This record represents the command to create a new workshop mechanic
 * with the necessary details.
 * </p>
 * 
 * @param fullName    The full name of the mechanic.
 * @param city        The city where the mechanic is located.
 * @param country     The country where the mechanic is located.
 * @param phone       The contact phone number of the mechanic.
 * @param companyName The name of the company the mechanic is associated with.
 * @param dni         The DNI (identification number) of the mechanic.
 */
public record CreateWorkshopMechanicCommand(String fullName, String city, String country,
        String phone, String companyName, String dni) {
}
