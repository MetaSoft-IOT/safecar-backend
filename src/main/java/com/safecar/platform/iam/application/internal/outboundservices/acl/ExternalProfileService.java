package com.safecar.platform.iam.application.internal.outboundservices.acl;



import com.safecar.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ExternalProfileService {
    private final ProfilesContextFacade profileContextFacade;

    public ExternalProfileService(ProfilesContextFacade profileContextFacade) {
        this.profileContextFacade = profileContextFacade;
    }


    public UUID createDriver(String fullName, String city, String country,
                             String phone, String dni, UUID userId) {
        return profileContextFacade.createDriver(
                fullName, city, country, phone, dni, userId
        );
    }

    public UUID createDistributor(String fullName, String city, String country,
                                  String phone, String companyName, String dni, UUID userId){
        return profileContextFacade.createMechanic(
                fullName, city, country, phone, companyName, dni, userId
        );
    }
}
