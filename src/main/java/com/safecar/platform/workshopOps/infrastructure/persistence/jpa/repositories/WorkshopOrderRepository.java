package com.safecar.platform.workshopOps.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.workshopOps.domain.model.aggregates.WorkshopOrder;
import com.safecar.platform.workshopOps.domain.model.valueobjects.WorkOrderStatus;
import com.safecar.platform.workshopOps.domain.model.valueobjects.WorkshopId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Workshop Order Repository - JPA Repository to manage WorkshopOrder entities.
 */
@Repository
public interface WorkshopOrderRepository extends JpaRepository<WorkshopOrder, Long> {
    /**
     * Finds all WorkshopOrders for a given WorkshopId.
     * @param workshop the id of the workshop
     * @return list of WorkshopOrders associated with the specified workshop
     */
    List<WorkshopOrder> findByWorkshop(WorkshopId workshop);
    /**
     * Finds all WorkshopOrders for a given WorkshopId and WorkOrderStatus.
     * @param workshop the id of the workshop
     * @param status the status of the work orders to filter by
     * @return list of WorkshopOrders associated with the specified workshop and status
     */
    List<WorkshopOrder> findByWorkshopAndStatus(WorkshopId workshop, WorkOrderStatus status);
    /**
     * Checks if a WorkshopOrder exists by code value and workshop id.
     * @param codeValue the code value of the work order
     * @param workshopId the id of the workshop
     * @return true if the WorkshopOrder exists, false otherwise
     */
    boolean existsByCodeValueAndWorkshop_WorkshopId(String codeValue, Long workshopId);
    /**
     * Finds a WorkshopOrder by code value and workshop id.
     * @param codeValue the code value of the work order
     * @param workshopId the id of the workshop
     * @return an optional WorkshopOrder
     */
    Optional<WorkshopOrder> findByCodeValueAndWorkshop_WorkshopId(String codeValue, Long workshopId);
}
