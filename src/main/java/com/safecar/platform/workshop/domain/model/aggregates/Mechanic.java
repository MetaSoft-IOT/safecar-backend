package com.safecar.platform.workshop.domain.model.aggregates;

import com.safecar.platform.workshop.domain.model.commands.CreateMechanicCommand;
import com.safecar.platform.shared.domain.model.valueobjects.ProfileId;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

/**
 * Mechanic aggregate root entity
 * @summary
 * This entity represents the mechanic aggregate root entity in the Workshop BC.
 * It contains the profile ID (reference to PersonProfile in Profiles BC) and
 * workshop-specific data like company name, specializations, and experience.
 * @see ProfileId
 * @see AuditableAbstractAggregateRoot
 * @since 1.0
 */
@Entity
@Getter
public class Mechanic extends AuditableAbstractAggregateRoot<Mechanic> {
    
    @Embedded
    @Column(name = "profile_id")
    private ProfileId profileId;

    @NotBlank(message = "Company name is required")
    private String companyName;

    private String specializations;

    @NotNull
    @PositiveOrZero(message = "Years of experience must be zero or positive")
    private Integer yearsOfExperience;

    /**
     * Default constructor for JPA
     */
    protected Mechanic() {
        super();
    }

    /**
     * Constructor with ProfileId
     * @param profileId the profile id from Profiles BC
     */
    public Mechanic(Long profileId) {
        this();
        this.profileId = new ProfileId(profileId);
        this.yearsOfExperience = 0;
    }

    /**
     * Constructor with ProfileId value object
     * @param profileId the profile id value object
     * @see ProfileId
     */
    public Mechanic(ProfileId profileId) {
        this();
        this.profileId = profileId;
        this.yearsOfExperience = 0;
    }

    /**
     * Constructor from CreateMechanicCommand
     * @param command the command containing mechanic creation data
     */
    public Mechanic(CreateMechanicCommand command) {
        this();
        this.profileId = new ProfileId(command.profileId());
        this.companyName = command.companyName();
        this.specializations = command.specializations();
        this.yearsOfExperience = command.yearsOfExperience() != null ? command.yearsOfExperience() : 0;
    }

    /**
     * Get profile id
     * @return the profile id from Profiles BC
     */
    public Long getProfileId() {
        return this.profileId.profileId();
    }

    /**
     * Update mechanic specializations
     * @param specializations new specializations
     */
    public void updateSpecializations(String specializations) {
        this.specializations = specializations;
    }

    /**
     * Update years of experience
     * @param yearsOfExperience new years of experience
     */
    public void updateYearsOfExperience(Integer yearsOfExperience) {
        if (yearsOfExperience != null && yearsOfExperience >= 0) {
            this.yearsOfExperience = yearsOfExperience;
        }
    }

    /**
     * Update company name
     * @param companyName new company name
     */
    public void updateCompanyName(String companyName) {
        if (companyName != null && !companyName.trim().isEmpty()) {
            this.companyName = companyName.trim();
        }
    }
}