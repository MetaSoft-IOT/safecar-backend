package com.safecar.platform.iam.domain.model.commands;

public record SignUpWorkshopCommand(String fullName, String email, String password,
                                    String city, String country, String phone, String companyName, String ruc) {
    public SignUpWorkshopCommand {
        if (!email.contains("@"))
            throw new RuntimeException("Invalid email");
        if (phone.length() != 9 || !phone.matches("\\d+")) {
            throw new RuntimeException("Invalid phone number: must contain exactly 9 digits.");
        }
        if (ruc.length() != 11 || !ruc.matches("\\d+")) {
            throw new RuntimeException("Invalid RUC number: must contain exactly 11 digits.");
        }
    }
}
