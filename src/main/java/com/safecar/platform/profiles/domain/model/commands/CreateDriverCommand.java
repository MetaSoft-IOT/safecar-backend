package com.safecar.platform.profiles.domain.model.commands;

/**
 * Create Driver Command
 * <p>
 * This command is used to create a new driver profile in the system.
 * </p>
 *
 * @param fullName the full name of the driver
 * @param city     the city where the driver is located
 * @param country  the country where the driver is located
 * @param phone    the phone number of the driver
 * @param dni      the national identification number of the driver
 */
public record CreateDriverCommand(String fullName, String city, String country,
        String phone, String dni) {
}
