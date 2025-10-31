package com.safecar.platform.deviceManagement.application.internal.outboundservices.acl;


import com.safecar.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;


@Service("externalProfileServiceFields")
public class ExternalProfileService {
    private final ProfilesContextFacade profilesContextFacade;

    public ExternalProfileService(ProfilesContextFacade profilesContextFacade) {
        this.profilesContextFacade = profilesContextFacade;
    }

    public void exitsAgriculturalProducer(Long userId) {
        boolean exists = profilesContextFacade.exitsDriverByUserId(userId);
        if (!exists) {
            throw new IllegalArgumentException("Driver not found with id %s".formatted(userId));
        }
    }
}
