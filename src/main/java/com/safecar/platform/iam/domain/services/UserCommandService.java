package com.safecar.platform.iam.domain.services;

import java.util.Optional;

import com.safecar.platform.iam.domain.model.commands.SignUpDriverCommand;
import com.safecar.platform.iam.domain.model.commands.SignUpWorkshopCommand;
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
    Optional<ImmutablePair<User, String>> handle(SignInCommand command);
    //Optional<User> handle(SignUpCommand command);
    Optional<User> handle(SignUpDriverCommand command);
    Optional<User> handle(SignUpWorkshopCommand command);
}
