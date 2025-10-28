package com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.profiles.domain.model.aggregates.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, Long> {
    Optional<Mechanic> findMechanicByUserId(Long userId);
    boolean existsByPhone_Phone(String phone);
    boolean existsByDni(String dni);
    boolean existsByUserId(Long userId);

}
