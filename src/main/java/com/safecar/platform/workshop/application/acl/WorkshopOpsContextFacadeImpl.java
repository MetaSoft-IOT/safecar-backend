package com.safecar.platform.workshop.application.acl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.safecar.platform.workshop.domain.model.queries.GetAppointmentByIdQuery;
import com.safecar.platform.workshop.domain.model.queries.GetTelemetryRecordByIdQuery;
import com.safecar.platform.workshop.domain.model.queries.GetWorkOrderByIdQuery;
import com.safecar.platform.workshop.domain.model.queries.GetWorkshopByIdQuery;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;
import com.safecar.platform.workshop.domain.services.VehicleTelemetryQueryService;
import com.safecar.platform.workshop.domain.services.WorkshopAppointmentQueryService;
import com.safecar.platform.workshop.domain.services.WorkshopOperationQueryService;
import com.safecar.platform.workshop.domain.services.WorkshopOrderQueryService;
import com.safecar.platform.workshop.interfaces.acl.WorkshopOpsContextFacade;

/**
 * WorkshopOpsContextFacadeImpl
 * <p>
 *     This class provides the implementation of the WorkshopOpsContextFacade interface.
 *     This class is used by other bounded contexts to interact with the WorkshopOps module
 *     for workshop operations validation and information retrieval.
 * </p>
 */
@Service
public class WorkshopOpsContextFacadeImpl implements WorkshopOpsContextFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkshopOpsContextFacadeImpl.class);

    private final WorkshopAppointmentQueryService appointmentQueryService;
    private final WorkshopOrderQueryService workOrderQueryService;
    private final WorkshopOperationQueryService workshopOperationQueryService;
    private final VehicleTelemetryQueryService vehicleTelemetryQueryService;

    /**
     * Constructor
     * @param appointmentQueryService the {@link WorkshopAppointmentQueryService} appointment query service
     * @param workOrderQueryService the {@link WorkshopOrderQueryService} work order query service
     * @param workshopOperationQueryService the {@link WorkshopOperationQueryService} workshop operation query service
     * @param vehicleTelemetryQueryService the {@link VehicleTelemetryQueryService} vehicle telemetry query service
     */
    public WorkshopOpsContextFacadeImpl(WorkshopAppointmentQueryService appointmentQueryService, 
                                       WorkshopOrderQueryService workOrderQueryService,
                                       WorkshopOperationQueryService workshopOperationQueryService,
                                       VehicleTelemetryQueryService vehicleTelemetryQueryService) {
        this.appointmentQueryService = appointmentQueryService;
        this.workOrderQueryService = workOrderQueryService;
        this.workshopOperationQueryService = workshopOperationQueryService;
        this.vehicleTelemetryQueryService = vehicleTelemetryQueryService;
    }

    // inherited javadoc
    @Override
    public boolean validateWorkshopOperationExists(Long workshopId) {
        if (workshopId == null || workshopId <= 0) {
            return false;
        }
        
        var query = new GetWorkshopByIdQuery(new WorkshopId(workshopId, "Workshop " + workshopId));
        var result = workshopOperationQueryService.handle(query);
        return result.isPresent();
    }

    // inherited javadoc
    @Override
    public String fetchWorkshopOperationDisplayName(Long workshopId) {
        if (workshopId == null || workshopId <= 0) {
            return String.format("Invalid Workshop ID: %s", workshopId);
        }
        
        var query = new GetWorkshopByIdQuery(new WorkshopId(workshopId, "Workshop " + workshopId));
        var result = workshopOperationQueryService.handle(query);
        
        if (result.isPresent()) {
            var operation = result.get();
            return operation.getWorkshop().displayName();
        }
        
        LOGGER.warn("WorkshopOperation not found for workshop ID: {}", workshopId);
        return String.format("Unknown Workshop #%d", workshopId);
    }

    // inherited javadoc
    @Override
    public boolean validateWorkshopAppointmentExists(Long appointmentId) {
        var query = new GetAppointmentByIdQuery(appointmentId);
        var result = appointmentQueryService.handle(query);
        return result.isPresent();
    }

    // inherited javadoc
    @Override
    public boolean validateWorkshopOrderExists(Long workOrderId) {
        var query = new GetWorkOrderByIdQuery(workOrderId);
        var result = workOrderQueryService.handle(query);
        return result.isPresent();
    }

    // inherited javadoc
    @Override
    public boolean validateVehicleTelemetryExists(Long telemetryId) {
        var query = new GetTelemetryRecordByIdQuery(telemetryId);
        var result = vehicleTelemetryQueryService.handle(query);
        return result.isPresent();
    }

    // inherited javadoc
    @Override
    public int fetchServiceBayCountByWorkshop(Long workshopId) {
        if (workshopId == null || workshopId <= 0) {
            return 0;
        }
        
        var query = new GetWorkshopByIdQuery(new WorkshopId(workshopId, "Workshop " + workshopId));
        var result = workshopOperationQueryService.handle(query);
        
        if (result.isPresent()) {
            var operation = result.get();
            return operation.getBaysCount();
        }
        
        LOGGER.warn("WorkshopOperation not found for workshop ID: {}, returning 0 service bays", workshopId);
        return 0;
    }
}
