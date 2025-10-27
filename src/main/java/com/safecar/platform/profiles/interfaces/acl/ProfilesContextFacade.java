package com.safecar.platform.profiles.interfaces.acl;

import java.util.UUID;

public interface ProfilesContextFacade {


    UUID createDriver(String fullName, String city, String country,
                      String phone, String dni, UUID userId);

    UUID createMechanic(String fullName, String city, String country,
                        String phone, String companyName, String dni, UUID userId);

    boolean exitsDriverByUserId(UUID userId);

    boolean exitsMechanicByUserId(UUID userId);
}
