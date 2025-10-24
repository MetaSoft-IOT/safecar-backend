package com.safecar.platform.iam.application.internal.queryservices;

import org.springframework.stereotype.Service;

import com.safecar.platform.iam.domain.model.entities.Role;
import com.safecar.platform.iam.domain.model.queries.GetAllRolesQuery;
import com.safecar.platform.iam.domain.services.RoleQueryService;
import com.safecar.platform.iam.infrastructure.persistence.jpa.repositories.RoleRepository;

import java.util.List;

/**
 * RoleQueryServiceImpl
 * <p>
 *     Implementation of the {@link RoleQueryService} interface.
 * </p>
 */
@Service
public class RoleQueryServiceImpl implements RoleQueryService {
    private final RoleRepository roleRepository;

    /**
     * Constructor
     * @param roleRepository the {@link RoleRepository} instance
     */
    public RoleQueryServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Handles the {@link GetAllRolesQuery} query.
     * @param query the {@link GetAllRolesQuery} instance
     * @return the list of {@link Role} instances
     */
    @Override
    public List<Role> handle(GetAllRolesQuery query) {
        return roleRepository.findAll();
    }
}