package com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories;


import com.safecar.platform.profiles.domain.model.aggregates.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findDriverByUserId(Long userId);
    // Verificar existencia de DNI
    boolean existsByDni_Dni(String dni);

    // Verificar existencia de Phone
    boolean existsByPhone_Phone(String phone);

    boolean existsByUserId(Long userId);
}
