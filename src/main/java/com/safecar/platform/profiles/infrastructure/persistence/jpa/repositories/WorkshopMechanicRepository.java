package com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.profiles.domain.model.aggregates.WorkshopMechanic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkshopMechanicRepository extends JpaRepository<WorkshopMechanic, Long> {
    Optional<WorkshopMechanic> findWorkshopMechanicByUserId(Long userId);
    boolean existsByPhone_Phone(String phone);
    boolean existsByDni(String dni);
    boolean existsByUserId(Long userId);
}
