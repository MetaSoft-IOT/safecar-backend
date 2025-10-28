package com.safecar.platform.profiles.interfaces.acl;

import java.util.UUID;

public interface ProfilesContextFacade {


    Long createDriver(String fullName, String city, String country,
                      String phone, String dni, Long userId);

    Long createMechanic(String fullName, String city, String country,
                        String phone, String companyName, String dni, Long userId);

    boolean exitsDriverByUserId(Long userId);

    boolean exitsMechanicByUserId(Long userId);
}
