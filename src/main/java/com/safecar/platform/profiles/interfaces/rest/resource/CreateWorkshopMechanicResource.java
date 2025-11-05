package com.safecar.platform.profiles.interfaces.rest.resource;

/**
 * Create Workshop Mechanic Resource
 * <p>
 * Represents the data required to create a new workshop mechanic profile.
 * </p>
 * 
 * @param fullName    The full name of the workshop mechanic.
 * @param city        The city where the workshop mechanic is located.
 * @param country     The country where the workshop mechanic is located.
 * @param phone       The contact phone number of the workshop mechanic.
 * @param companyName The name of the company the workshop mechanic is
 *                    associated with.
 * @param dni         The national identification number of the workshop
 *                    mechanic.
 */
public record CreateWorkshopMechanicResource(
        String fullName,
        String city,
        String country,
        String phone,
        String companyName,
        String dni) {
}