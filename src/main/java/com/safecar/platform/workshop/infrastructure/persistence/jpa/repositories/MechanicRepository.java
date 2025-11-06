package com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.workshop.domain.model.aggregates.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, Long> {
    Optional<Mechanic> findByProfileId_ProfileId(Long profileId);
    boolean existsByProfileId_ProfileId(Long profileId);
    boolean existsByCompanyName(String companyName);
}