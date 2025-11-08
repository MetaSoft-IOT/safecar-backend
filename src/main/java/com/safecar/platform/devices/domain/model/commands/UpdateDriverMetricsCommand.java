package com.safecar.platform.devices.domain.model.commands;

/**
 * Update Driver Metrics Command
 *
 * <p>
 * Command to update the metrics of a driver, including total vehicles and total
 * trips.
 * </p>
 *
 * @param profileId     the unique identifier of the driver's profile
 * @param totalVehicles the updated total number of vehicles associated with the
 *                      driver
 * @param totalTrips    the updated total number of trips completed by the
 *                      driver
 */
public record UpdateDriverMetricsCommand(Long profileId) {
}
