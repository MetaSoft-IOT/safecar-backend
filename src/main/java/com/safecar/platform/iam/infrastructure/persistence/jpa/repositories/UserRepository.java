package com.safecar.platform.iam.infrastructure.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safecar.platform.iam.domain.model.aggregates.User;

/**
 * Repository interface for managing {@link UserAggregate} entities.
 * <p>
 * Provides methods to perform CRUD operations and custom queries for users in the database.
 * </p>
 *
 * @author GonzaloQu3dena
 * @since 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String username);
    boolean existsByEmail(String username);
    boolean existsById(Long userId);
}
