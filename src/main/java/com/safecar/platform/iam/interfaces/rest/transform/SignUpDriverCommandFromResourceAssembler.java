package com.safecar.platform.iam.interfaces.rest.transform;


import com.safecar.platform.iam.domain.model.commands.SignUpDriverCommand;
import com.safecar.platform.iam.interfaces.rest.resources.SignUpDriverResource;

public class SignUpDriverCommandFromResourceAssembler {
    public static SignUpDriverCommand toCommandFromResource(SignUpDriverResource resource) {
        return new SignUpDriverCommand(resource.fullName(), resource.email() ,resource.password(),
                resource.city(), resource.country(), resource.phone(), resource.dni());
    }
}
