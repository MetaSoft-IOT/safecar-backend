package com.safecar.platform.iam.domain.services;

import java.util.List;
import java.util.Optional;

import com.safecar.platform.iam.domain.model.entities.Role;
import com.safecar.platform.iam.domain.model.queries.GetAllRolesQuery;
import com.safecar.platform.iam.domain.model.queries.GetRoleByNameQuery;

/**
 * Service interface for handling role-related query operations.
 * <p>
 * Provides methods to retrieve roles based on different query criteria.
 *
 * @author GonzaloQu3dena
 * @since 2025-10-06
 * @version 1.0.0
 */
public interface RoleQueryService {
    List<Role> handle(GetAllRolesQuery query);
    Optional<Role> handle(GetRoleByNameQuery query);
}
