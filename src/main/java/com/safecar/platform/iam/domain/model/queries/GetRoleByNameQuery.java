package com.safecar.platform.iam.domain.model.queries;


import com.safecar.platform.iam.domain.model.valueobjects.Roles;

public record GetRoleByNameQuery(Roles name) {
}
