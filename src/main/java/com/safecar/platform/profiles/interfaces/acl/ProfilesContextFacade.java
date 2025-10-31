package com.safecar.platform.profiles.interfaces.acl;

public interface ProfilesContextFacade {


    Long createDriver(String fullName, String city, String country,
                      String phone, String dni, Long userId);

    Long createWorkshop(String fullName, String city, String country,
                        String phone, String companyName, String dni, Long userId);

    boolean exitsDriverByUserId(Long userId);

    boolean exitsWorkshopByUserId(Long userId);
}
