package com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.profiles.domain.model.aggregates.PersonProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonProfileRepository extends JpaRepository<PersonProfile, Long> {
    Optional<PersonProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    boolean existsByDni_Dni(String dni);
    boolean existsByPhone_Phone(String phone);
}
