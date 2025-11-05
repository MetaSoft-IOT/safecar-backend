package com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safecar.platform.workshop.domain.model.entities.ServiceBay;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;

import java.util.List;

@Repository
public interface ServiceBayRepository extends JpaRepository<ServiceBay, Long> {
    List<ServiceBay> findByWorkshop(WorkshopId workshop);
}
