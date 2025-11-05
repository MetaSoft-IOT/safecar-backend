package com.safecar.platform.profiles.interfaces.rest.resource;

/**
 * Driver Resource
 * <p>
 * Represents the data structure for a driver in the SafeCar platform.
 * </p>
 * 
 * @param userId   the unique identifier of the user
 * @param driverId the unique identifier of the driver
 * @param fullName the full name of the driver
 * @param city     the city where the driver is located
 * @param country  the country where the driver is located
 * @param phone    the contact phone number of the driver
 * @param dni      the national identification number of the driver
 */
public record DriverResource(
                Long userId,
                Long driverId,
                String fullName,
                String city,
                String country,
                String phone,
                String dni) {
}
