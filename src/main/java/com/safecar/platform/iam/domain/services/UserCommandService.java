package com.safecar.platform.iam.domain.services;

import java.util.Optional;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.safecar.platform.iam.domain.model.aggregates.User;
import com.safecar.platform.iam.domain.model.commands.SignInCommand;
import com.safecar.platform.iam.domain.model.commands.SignUpCommand;

/**
 * Service interface for handling user-related commands such as sign-up and sign-in.
 * <p>
 * Provides methods to process user registration and authentication commands.
 *
 * @author GonzaloQu3dena
 * @since 2025-10-06
 * @version 1.0.0
 */
public interface UserCommandService {

    /**
     * Handles the user sign-up command.
     *
     * @param command the sign-up command containing user registration details
     * @return an {@link Optional} containing the created {@link UserAggregate} if successful, or empty if not
     */
    Optional<User> handle(SignUpCommand command);

    /**
     * Handles the user sign-in command.
     *
     * @param command the sign-in command containing user authentication details
     * @return an {@link Optional} containing an {@link ImmutablePair} of the authenticated {@link UserAggregate}
     * and a generated authentication token if successful, or empty if authentication fails
     */
    Optional<ImmutablePair<User, String>> handle(SignInCommand command);
}