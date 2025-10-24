package com.safecar.platform.iam.interfaces.rest.transform;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.safecar.platform.iam.domain.model.entities.Role;

/**
 * Assembler to convert a list of role names to a list of Role entities.
 * <p>
 *     This class is used to convert a list of role names to a list of Role entities.
 * </p>
 */
public class RoleListFromStringAssembler {
    /**
     * Converts a list of role names to a list of Role entities.
     *
     * @param resourceList The list of role names to convert.
     * @return The list of Role entities.
     */
    public static Set<Role> toRoleListFromStringList(Set<String> resourceList) {
        return Objects.nonNull(resourceList) ? resourceList.stream()
                .map(Role::toRoleFromName).collect(Collectors.toSet()) : Set.of();
    }
}