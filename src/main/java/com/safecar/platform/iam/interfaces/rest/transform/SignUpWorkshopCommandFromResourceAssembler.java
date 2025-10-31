package com.safecar.platform.iam.interfaces.rest.transform;


import com.safecar.platform.iam.domain.model.commands.SignUpWorkshopCommand;
import com.safecar.platform.iam.interfaces.rest.resources.SignUpWorkshopResource;

public class SignUpWorkshopCommandFromResourceAssembler {
    public static SignUpWorkshopCommand toCommandFromResource(SignUpWorkshopResource resource) {
        return new SignUpWorkshopCommand(resource.fullName(), resource.email() ,resource.password(),
                resource.city(), resource.country(), resource.phone(), resource.companyName(),resource.ruc());
    }
}
