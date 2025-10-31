package com.safecar.platform.iam.infrastructure.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.safecar.platform.iam.domain.model.entities.Role;
import com.safecar.platform.iam.domain.model.valueobjects.Roles;

/**
 * Repository interface for managing {@link Role} entities.
 * <p>
 * Provides methods to perform CRUD operations and custom queries for roles in the database.
 * </p>
 *
 * @author GonzaloQu3dena
 * @since 1.0.0
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role> findByName(Roles name);
    boolean existsByName(Roles name);
}
