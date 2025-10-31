package com.safecar.platform.profiles.domain.model.commands;

public record CreateDriverCommand(String fullName, String city, String country,
                                  String phone, String dni) {
}
