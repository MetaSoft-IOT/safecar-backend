package com.safecar.platform.profiles.interfaces.rest.resource;

/**
 * Create Driver Resource
 * <p>
 * This record represents the data required to create a new driver profile.
 * It includes validation annotations to ensure data integrity.
 * </p>
 * 
 * @param fullName The full name of the driver.
 * @param city     The city where the driver is located.
 * @param country  The country where the driver is located.
 * @param phone    The contact phone number of the driver.
 * @param dni      The national identification number of the driver.
 */
public record CreateDriverResource(
        String fullName,
        String city,
        String country,
        String phone,
        String dni) {
}