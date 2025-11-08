package com.safecar.platform.profiles.domain.model.aggregates;


import com.safecar.platform.profiles.domain.model.commands.CreatePersonProfileCommand;
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
 * PersonProfile Aggregate
 * <p>
 * Represents a person's profile within the SafeCar platform including personal
 * details.
 * </p>
 */
@Setter
@Getter
@Entity
public class PersonProfile extends AuditableAbstractAggregateRoot<PersonProfile> {

    /**
     * User ID associated with this profile - Has to be positive and not null
     */
    @NotNull
    @Positive
    private Long userId;

    /**
     * Full name of the person - Cannot be blank
     */
    @NotBlank
    private String fullName;

    /**
     * City of residence - Cannot be blank
     */
    @NotBlank
    private String city;

    /**
     * Country of residence - Cannot be blank
     */
    @NotBlank
    private String country;

    /**
     * Phone value object representing the person's phone details
     */
    @Embedded
    private Phone phone;

    /**
     * Dni value object representing the person's national identification number
     */
    @Embedded
    private Dni dni;

    /**
     * Default constructor for JPA
     */
    protected PersonProfile() {
    }

    /**
     * Constructor to create PersonProfile from command and userId
     * 
     * @param command the create person profile command
     * @param userId  the associated user ID
     */
    public PersonProfile(CreatePersonProfileCommand command, Long userId) {
        this.userId = userId;
        this.fullName = command.fullName();
        this.city = command.city();
        this.country = command.country();
        this.phone = new Phone(command.phone());
        this.dni = new Dni(command.dni());
    }

    /**
     * Constructor with all fields
     * 
     * @param userId   the user ID
     * @param fullName the full name of the person
     * @param city     the city of residence
     * @param country  the country of residence
     * @param phone    the phone value object
     * @param dni      the dni value object
     */
    public PersonProfile(Long userId, String fullName, String city, String country, Phone phone, Dni dni) {
        this.userId = userId;
        this.fullName = fullName;
        this.city = city;
        this.country = country;
        this.phone = phone;
        this.dni = dni;
    }

    /**
     * Get phone number as string
     * 
     * @return phone number
     */
    public String getPhoneNumber() {
        return phone.phone();
    }

    /**
     * Get dni number as string
     * 
     * @return dni number
     */
    public String getDniNumber() {
        return dni.dni();
    }
}
