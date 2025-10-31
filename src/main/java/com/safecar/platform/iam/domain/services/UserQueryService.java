package com.safecar.platform.iam.domain.services;

import java.util.List;
import java.util.Optional;

import com.safecar.platform.iam.domain.model.aggregates.User;
import com.safecar.platform.iam.domain.model.queries.CheckUserByIdQuery;
import com.safecar.platform.iam.domain.model.queries.GetAllUsersQuery;
import com.safecar.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.safecar.platform.iam.domain.model.queries.GetUserByUsernameQuery;

/**
 * Service interface for handling user-related query operations.
 * <p>
 * Provides methods to retrieve users based on different query criteria.
 *
 * @author GonzaloQu3dena
 * @since 2025-10-06
 * @version 1.0.0
 */
public interface UserQueryService {
    List<User> handle(GetAllUsersQuery query);
    Optional<User> handle(GetUserByIdQuery query);
    Optional<User> handle(GetUserByUsernameQuery query);
    boolean handle(CheckUserByIdQuery query);
}
