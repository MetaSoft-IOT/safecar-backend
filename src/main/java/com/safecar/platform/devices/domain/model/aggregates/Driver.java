package com.safecar.platform.devices.domain.model.aggregates;

import com.safecar.platform.devices.domain.model.valueobjects.DriverId;
import com.safecar.platform.shared.domain.model.valueobjects.ProfileId;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

/**
 * Driver aggregate root entity
 * @summary
 * This entity represents the driver aggregate root entity.
 * It contains the driver record id, profile id, and driver-specific metrics.
 * The driver is associated with vehicles and can perform vehicle operations.
 * @see DriverId
 * @see ProfileId
 * @see AuditableAbstractAggregateRoot
 * @since 1.0
 */
@Entity
public class Driver extends AuditableAbstractAggregateRoot<Driver> {
    @Getter
    @Embedded
    @Column(name = "driver_id")
    private final DriverId driverId;

    @Embedded
    private ProfileId profileId;

    @Getter
    private Integer totalVehicles;

    @Getter
    private Integer totalTrips;

    /**
     * Default constructor
     */
    public Driver() {
        super();
        this.driverId = new DriverId();
        this.totalVehicles = 0;
        this.totalTrips = 0;
    }

    /**
     * Constructor with profile id
     * @param profileId the profile id
     */
    public Driver(Long profileId) {
        this();
        this.profileId = new ProfileId(profileId);
    }

    /**
     * Constructor with profile id
     * @param profileId the profile id
     *                  @see ProfileId
     */
    public Driver(ProfileId profileId) {
        this();
        this.profileId = profileId;
    }

    /**
     * Update metrics when a vehicle is assigned.
     * @summary
     * This method increments the total vehicles count.
     */
    public void updateMetricsOnVehicleAssigned() {
        this.totalVehicles = this.totalVehicles + 1;
    }

    /**
     * Update metrics when a trip is completed.
     * @summary
     * This method increments the total trips count.
     */
    public void updateMetricsOnTripCompleted() {
        this.totalTrips = this.totalTrips + 1;
    }

    /**
     * Get driver record id
     * @return the driver record id
     */
    public Long getDriverRecordId() {
        return this.driverId.driverId();
    }

    /**
     * Get profile id
     * @return the profile id
     */
    public Long getProfileId() {
        return this.profileId.profileId();
    }
}