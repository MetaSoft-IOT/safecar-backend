package com.safecar.platform.profiles.interfaces.rest.resource;

/**
 * Workshop Mechanic Resource
 * <p>
 * Represents a workshop mechanic with relevant details.
 * </p>
 * 
 * @param userId             the unique identifier of the user
 * @param workshopMechanicId the unique identifier of the workshop mechanic
 * @param fullName           the full name of the workshop mechanic
 * @param city               the city where the workshop mechanic is located
 * @param country            the country where the workshop mechanic is located
 * @param phone              the contact phone number of the workshop mechanic
 * @param companyName        the name of the company the workshop mechanic is
 *                           associated with
 * @param dni                the national identification number of the workshop
 *                           mechanic
 */
public record WorkshopMechanicResource(
                Long userId,
                Long workshopMechanicId,
                String fullName,
                String city,
                String country,
                String phone,
                String companyName,
                String dni) {
}
