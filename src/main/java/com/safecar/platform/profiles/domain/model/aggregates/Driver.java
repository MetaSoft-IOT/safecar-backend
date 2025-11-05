package com.safecar.platform.profiles.domain.model.aggregates;

import com.safecar.platform.profiles.domain.model.commands.CreateDriverCommand;
import com.safecar.platform.profiles.domain.model.valueobjects.Dni;
import com.safecar.platform.profiles.domain.model.valueobjects.Phone;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * Driver Aggregate
 * <p>
 * Represents a driver in the SafeCar platform. Contains personal information
 * such as full name, city, country, phone, DNI, and associated
 * user ID.
 * </p>
 */
@Setter
@Getter
@Entity
public class Driver extends AuditableAbstractAggregateRoot<Driver> {

    /*
     * Driver's full name
     */
    @NotBlank
    private String fullName;

    /**
     * Driver's city
     */
    @NotBlank
    private String city;

    /**
     * Driver's country
     */
    @NotBlank
    private String country;

    /**
     * Driver's phone number
     */
    @Embedded
    private Phone phone;

    /**
     * Driver's DNI (National Identification Document)
     */
    @Embedded
    private Dni dni;

    /**
     * Associated User ID
     */
    @NotNull(message = "UserId cannot be null")
    @Positive(message = "UserId must be positive")
    private Long userId;

    /**
     * Constructor to create a Driver from a CreateDriverCommand and associated
     * userId.
     * 
     * @param command CreateDriverCommand containing driver details
     * @param userId  Associated user ID
     */
    public Driver(CreateDriverCommand command, Long userId) {
        this.fullName = command.fullName();
        this.city = command.city();
        this.country = command.country();
        this.phone = new Phone(command.phone());
        this.dni = new Dni(command.dni());
        this.userId = userId;
    }

    /**
     * Default constructor for JPA
     */
    public Driver() {
    }

    /**
     * Get the driver's DNI as a String.
     * 
     * @return Driver's DNI
     */
    public String getDni() {
        return dni.dni();
    }

    /**
     * Get the driver's phone number as a String.
     * 
     * @return Driver's phone number
     */
    public String getPhone() {
        return phone.phone();
    }

}
