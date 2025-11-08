package com.safecar.platform.profiles.application.internal.outbounceservices.acl;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.safecar.platform.iam.interfaces.acl.IamContextFacade;

@Service("externalIamService")
public class ExternalIamService {
    private final IamContextFacade iamContextFacade;

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public Set<String> fetchUserRolesByUserId(Long userId) {
        return iamContextFacade.fetchUserRolesByUserId(userId);
    }
}
