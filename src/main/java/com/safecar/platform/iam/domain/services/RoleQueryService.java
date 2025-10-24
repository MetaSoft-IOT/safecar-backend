package com.safecar.platform.iam.domain.services;

import java.util.List;

import com.safecar.platform.iam.domain.model.entities.Role;
import com.safecar.platform.iam.domain.model.queries.GetAllRolesQuery;

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

    /**
     * Handles the query to retrieve all roles.
     *
     * @param query the query object for retrieving all roles
     * @return a list of all {@link Role} instances
     */
    List<Role> handle(GetAllRolesQuery query);
}