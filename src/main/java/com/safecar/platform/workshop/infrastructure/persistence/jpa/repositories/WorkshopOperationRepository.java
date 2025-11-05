package com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safecar.platform.workshop.domain.model.aggregates.WorkshopOperation;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;

import java.util.Optional;

@Repository
public interface WorkshopOperationRepository extends JpaRepository<WorkshopOperation, Long> {
    Optional<WorkshopOperation> findByWorkshop(WorkshopId workshop);
}
