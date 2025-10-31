package com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.profiles.domain.model.aggregates.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkshopRepository extends JpaRepository<Workshop, Long> {
    Optional<Workshop> findWorkshopByUserId(Long userId);
    boolean existsByPhone_Phone(String phone);
    boolean existsByDni(String dni);
    boolean existsByUserId(Long userId);

}
