package com.safecar.platform.workshopOps.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.workshopOps.domain.model.aggregates.WorkshopOperation;
import com.safecar.platform.workshopOps.domain.model.valueobjects.WorkshopId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkshopOperationRepository extends JpaRepository<WorkshopOperation, Long> {
    Optional<WorkshopOperation> findByWorkshop(WorkshopId workshop);
}
