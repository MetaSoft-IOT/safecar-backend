package com.safecar.platform.iam.domain.model.commands;

/**
 * SignInCommand
 * <p>
 * Command record for user sign-in containing email and password.
 * </p>
 * 
 * @author GonzaloQu3dena
 * @version 1.0
 * @since 2025-10-05
 */
public record SignInCommand(String email, String password) {
}
