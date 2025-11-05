package com.safecar.platform.devices.application.internal.outboundservices.acl;

import com.safecar.platform.workshopOps.interfaces.acl.WorkshopOpsContextFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * External Workshop Service (Anti-Corruption Layer).
 * 
 * This service acts as an ACL between the Devices and WorkshopOps bounded contexts,
 * providing device-specific operations that interact with workshop operations.
 * It translates between the domains and ensures loose coupling.
 */
@Service
public class ExternalWorkshopService {
    
    private static final Logger logger = LoggerFactory.getLogger(ExternalWorkshopService.class);
    
    private final WorkshopOpsContextFacade workshopOpsContextFacade;

    /**
     * Constructor for ExternalWorkshopService.
     * 
     * @param workshopOpsContextFacade the workshop ops context facade
     */
    public ExternalWorkshopService(WorkshopOpsContextFacade workshopOpsContextFacade) {
        this.workshopOpsContextFacade = workshopOpsContextFacade;
    }

    /**
     * Validates if a workshop operation exists and is active.
     * Used when creating vehicles that need to be assigned to specific workshops.
     * 
     * @param workshopId the workshop ID to validate
     * @return true if workshop operation exists and is active, false otherwise
     */
    public boolean validateWorkshopExists(Long workshopId) {
        try {
            return workshopOpsContextFacade.validateWorkshopOperationExists(workshopId);
        } catch (Exception e) {
            logger.error("Error validating workshop existence for ID {}: {}", workshopId, e.getMessage());
            return false;
        }
    }

    /**
     * Fetches the display name of a workshop for vehicle assignment purposes.
     * 
     * @param workshopId the workshop ID
     * @return the workshop display name if found, "Unknown Workshop" otherwise
     */
    public String fetchWorkshopDisplayName(Long workshopId) {
        try {
            return workshopOpsContextFacade.fetchWorkshopOperationDisplayName(workshopId);
        } catch (Exception e) {
            logger.error("Error fetching workshop display name for ID {}: {}", workshopId, e.getMessage());
            return "Unknown Workshop";
        }
    }

    /**
     * Validates if an appointment exists for a specific vehicle.
     * Used to check if a vehicle has pending service appointments before updates.
     * 
     * @param appointmentId the appointment ID to validate
     * @return true if appointment exists, false otherwise
     */
    public boolean validateAppointmentExists(Long appointmentId) {
        try {
            return workshopOpsContextFacade.validateWorkshopAppointmentExists(appointmentId);
        } catch (Exception e) {
            logger.error("Error validating appointment existence for ID {}: {}", appointmentId, e.getMessage());
            return false;
        }
    }

    /**
     * Validates if a work order exists for a specific vehicle.
     * Used to check if a vehicle has active work orders before transfers or updates.
     * 
     * @param workOrderId the work order ID to validate
     * @return true if work order exists, false otherwise
     */
    public boolean validateWorkOrderExists(Long workOrderId) {
        try {
            return workshopOpsContextFacade.validateWorkshopOrderExists(workOrderId);
        } catch (Exception e) {
            logger.error("Error validating work order existence for ID {}: {}", workOrderId, e.getMessage());
            return false;
        }
    }

    /**
     * Validates if telemetry data exists for a vehicle.
     * Used when vehicles need to verify they have telemetry history.
     * 
     * @param telemetryId the telemetry aggregate ID to validate
     * @return true if telemetry exists, false otherwise
     */
    public boolean validateVehicleTelemetryExists(Long telemetryId) {
        try {
            return workshopOpsContextFacade.validateVehicleTelemetryExists(telemetryId);
        } catch (Exception e) {
            logger.error("Error validating vehicle telemetry existence for ID {}: {}", telemetryId, e.getMessage());
            return false;
        }
    }

    /**
     * Fetches the number of service bays available at a workshop.
     * Used to determine workshop capacity for vehicle assignments.
     * 
     * @param workshopId the workshop ID
     * @return number of service bays, 0 if workshop not found
     */
    public int fetchWorkshopServiceBayCount(Long workshopId) {
        try {
            return workshopOpsContextFacade.fetchServiceBayCountByWorkshop(workshopId);
        } catch (Exception e) {
            logger.error("Error fetching service bay count for workshop {}: {}", workshopId, e.getMessage());
            return 0;
        }
    }

    /**
     * Checks if a workshop has available capacity for new vehicles.
     * Combines validation and capacity checking.
     * 
     * @param workshopId the workshop ID
     * @return true if workshop exists and has available service bays, false otherwise
     */
    public boolean hasWorkshopCapacity(Long workshopId) {
        try {
            boolean workshopExists = validateWorkshopExists(workshopId);
            if (!workshopExists) {
                return false;
            }
            int serviceBayCount = fetchWorkshopServiceBayCount(workshopId);
            return serviceBayCount > 0;
        } catch (Exception e) {
            logger.error("Error checking workshop capacity for ID {}: {}", workshopId, e.getMessage());
            return false;
        }
    }

    /**
     * Gets workshop information for display in vehicle management screens.
     * 
     * @param workshopId the workshop ID
     * @return formatted workshop information string
     */
    public String getWorkshopInfo(Long workshopId) {
        try {
            String displayName = fetchWorkshopDisplayName(workshopId);
            int serviceBays = fetchWorkshopServiceBayCount(workshopId);
            return String.format("%s (%d service bays)", displayName, serviceBays);
        } catch (Exception e) {
            logger.error("Error getting workshop info for ID {}: {}", workshopId, e.getMessage());
            return "Unknown Workshop (0 service bays)";
        }
    }
}