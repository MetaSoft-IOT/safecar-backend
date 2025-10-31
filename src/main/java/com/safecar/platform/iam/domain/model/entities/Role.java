package com.safecar.platform.iam.domain.model.entities;

import java.util.List;
import java.util.Set;

import lombok.*;

import jakarta.persistence.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import com.safecar.platform.iam.domain.model.valueobjects.Roles;

/**
 * Entity representing a user role within the system.
 * <p>
 * Maps to the {@code roles} table and encapsulates the role's unique identifier
 * and name, provides utility
 * methods for default role assignment, role creation, and validation.
 * </p>
 * 
 * @author GonzaloQu3dena
 * @version 1.0
 * @since 2025-10-05
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@With
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Roles name;

    public Role(Roles name) {
        this.name = name;
    }

    /**
     * Get the name of the role as a string
     * @return the name of the role as a string
     */
    public String getStringName() {
        return name.name();
    }

    /**
     * Get the default role
     * @return the default role
     */
    public static Role getDefaultRole() {
        return new Role(Roles.ROLE_ADMIN);
    }

    /**
     * Get the role from its name
     * @param name the name of the role
     * @return the role
     */
    public static Role toRoleFromName(String name) {
        return new Role(Roles.valueOf(name));
    }

    /**
     * Validate the role set
     * <p>
     *     This method validates the role set and returns the default role if the set is empty.
     * </p>
     * @param roles the role set
     * @return the role set
     */
    public static List<Role> validateRoleSet(List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return List.of(getDefaultRole());
        }
        return roles;
    }

}