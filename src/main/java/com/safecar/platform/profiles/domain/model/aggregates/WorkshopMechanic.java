package com.safecar.platform.profiles.domain.model.aggregates;

import com.safecar.platform.profiles.domain.model.commands.CreateWorkshopMechanicCommand;
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
 * Workshop Mechanic Aggregate
 * <p>
 * Represents a workshop mechanic in the SafeCar platform. Contains personal
 * information
 * such as full name, city, country, phone, company name, DNI, and associated
 * user ID.
 * </p>
 */
@Setter
@Getter
@Entity
public class WorkshopMechanic extends AuditableAbstractAggregateRoot<WorkshopMechanic> {

    /**
     * Associated User ID
     */
    @NotNull
    @Positive
    private Long userId;

    /**
     * Workshop Mechanic's full name
     */
    @NotBlank
    private String fullName;

    /**
     * Workshop Mechanic's city
     */
    @NotBlank
    private String city;

    /**
     * Workshop Mechanic's country
     */
    @NotBlank
    private String country;

    /**
     * Workshop Mechanic's phone number
     */
    @Embedded
    private Phone phone;

    /**
     * Workshop Mechanic's company name
     */
    @NotBlank
    private String companyName;

    /**
     * Workshop Mechanic's DNI (National Identification Document)
     */
    @Embedded
    private Dni dni;

    /**
     * Protected no-args constructor for JPA
     */
    protected WorkshopMechanic() {
    }

    /**
     * Constructor to create a WorkshopMechanic from a CreateWorkshopMechanicCommand
     * and associated userId.
     * 
     * @param command CreateWorkshopMechanicCommand
     * @param userId  Associated user ID
     */
    public WorkshopMechanic(CreateWorkshopMechanicCommand command, Long userId) {
        this.userId = userId;
        this.fullName = command.fullName();
        this.city = command.city();
        this.country = command.country();
        this.phone = new Phone(command.phone());
        this.companyName = command.companyName();
        this.dni = new Dni(command.dni());
    }

    /**
     * Get full name
     * 
     * @return Full name of the workshop mechanic
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Get city
     * 
     * @return City of the workshop mechanic
     */
    public String getCity() {
        return city;
    }

    /**
     * Get country
     * 
     * @return Country of the workshop mechanic
     */
    public String getCountry() {
        return country;
    }

    /**
     * Get phone number
     * 
     * @return Phone number of the workshop mechanic
     */
    public String getPhone() {
        return phone != null ? phone.phone() : null;
    }

    /**
     * Get company name
     * 
     * @return Company name of the workshop mechanic
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Get DNI
     * 
     * @return DNI of the workshop mechanic
     */
    public String getDni() {
        return dni != null ? dni.dni() : null;
    }
}
