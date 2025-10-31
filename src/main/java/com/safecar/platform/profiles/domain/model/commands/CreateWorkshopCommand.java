package com.safecar.platform.profiles.domain.model.commands;

public record CreateWorkshopCommand(String fullName, String city, String country,
                                    String phone, String companyName, String dni) {
}
