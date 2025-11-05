package com.safecar.platform.workshop.interfaces.acl;

/**
 * WorkshopOpsContextFacade
 * <p>
 *     This interface provides the methods to interact with the WorkshopOps context.
 *     It provides methods to access workshop operations, appointments, work orders, and telemetry data.
 *     The implementation will be provided by the WorkshopOps module.
 *     This interface is used by other bounded contexts to interact with workshop operations.
 * </p>
 */
public interface WorkshopOpsContextFacade {

    /**
     * validateWorkshopOperationExists
     * <p>
     *     This method validates if a WorkshopOperation exists by workshopId.
     *     WorkshopOperation is the aggregate that manages operations for a specific workshop.
     * </p>
     * @param workshopId the workshop ID to validate
     * @return true if WorkshopOperation exists, false otherwise
     */
    boolean validateWorkshopOperationExists(Long workshopId);

    /**
     * fetchWorkshopOperationDisplayName
     * <p>
     *     This method fetches the workshop display name from WorkshopOperation by workshop ID.
     * </p>
     * @param workshopId the workshop ID
     * @return the workshop display name if found, default name otherwise
     */
    String fetchWorkshopOperationDisplayName(Long workshopId);

    /**
     * validateWorkshopAppointmentExists
     * <p>
     *     This method validates if a WorkshopAppointment exists by appointmentId.
     * </p>
     * @param appointmentId the appointment ID to validate
     * @return true if WorkshopAppointment exists, false otherwise
     */
    boolean validateWorkshopAppointmentExists(Long appointmentId);

    /**
     * validateWorkshopOrderExists
     * <p>
     *     This method validates if a WorkshopOrder exists by workOrderId.
     * </p>
     * @param workOrderId the work order ID to validate
     * @return true if WorkshopOrder exists, false otherwise
     */
    boolean validateWorkshopOrderExists(Long workOrderId);

    /**
     * validateVehicleTelemetryExists
     * <p>
     *     This method validates if VehicleTelemetry aggregate exists by telemetryId.
     * </p>
     * @param telemetryId the telemetry aggregate ID to validate
     * @return true if VehicleTelemetry exists, false otherwise
     */
    boolean validateVehicleTelemetryExists(Long telemetryId);

    /**
     * fetchServiceBayCountByWorkshop
     * <p>
     *     This method fetches the number of allocated service bays for a workshop operation.
     * </p>
     * @param workshopId the workshop ID
     * @return the number of service bays allocated, 0 if workshop operation not found
     */
    int fetchServiceBayCountByWorkshop(Long workshopId);
}
