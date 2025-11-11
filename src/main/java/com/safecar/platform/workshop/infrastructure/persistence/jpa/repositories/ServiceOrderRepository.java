package com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safecar.platform.workshop.domain.model.aggregates.ServiceOrder;
import com.safecar.platform.workshop.domain.model.valueobjects.ServiceOrderStatus;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;

import java.util.List;
import java.util.Optional;

/**
 * Service Order Repository - JPA Repository to manage ServiceOrder entities.
 */
@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {
    /**
     * Finds all ServiceOrders for a given WorkshopId.
     * @param workshop the id of the workshop
     * @return list of ServiceOrders associated with the specified workshop
     */
    List<ServiceOrder> findByWorkshop(WorkshopId workshop);
    /**
     * Finds all ServiceOrders for a given WorkshopId and ServiceOrderStatus.
     * @param workshop the id of the workshop
     * @param status the status of the service orders to filter by
     * @return list of ServiceOrders associated with the specified workshop and status
     */
    List<ServiceOrder> findByWorkshopAndStatus(WorkshopId workshop, ServiceOrderStatus status);
    /**
     * Checks if a ServiceOrder exists by code value and workshop id.
     * @param codeValue the code value of the service order
     * @param workshopId the id of the workshop
     * @return true if the ServiceOrder exists, false otherwise
     */
    boolean existsByCodeValueAndWorkshop_WorkshopId(String codeValue, Long workshopId);
    /**
     * Finds a ServiceOrder by code value and workshop id.
     * @param codeValue the code value of the service order
     * @param workshopId the id of the workshop
     * @return an optional ServiceOrder
     */
    Optional<ServiceOrder> findByCodeValueAndWorkshop_WorkshopId(String codeValue, Long workshopId);
}
