package com.safecar.platform.workshopOps.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.workshopOps.domain.model.entities.ServiceBay;
import com.safecar.platform.workshopOps.domain.model.valueobjects.WorkshopId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceBayRepository extends JpaRepository<ServiceBay, Long> {
    List<ServiceBay> findByWorkshop(WorkshopId workshop);
}
