package com.safecar.platform.profiles.domain.model.commands;

/**
 * Command to create a PersonProfile in the Profiles BC.
 */
public record CreatePersonProfileCommand(String fullName, String city, String country, String phone, String dni) {
}
