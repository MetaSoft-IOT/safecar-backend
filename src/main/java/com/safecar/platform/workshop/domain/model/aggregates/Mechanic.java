package com.safecar.platform.workshop.domain.model.aggregates;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.safecar.platform.workshop.domain.model.commands.CreateMechanicCommand;
import com.safecar.platform.workshop.domain.model.entities.Specialization;
import com.safecar.platform.shared.domain.model.valueobjects.ProfileId;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

/**
 * Mechanic aggregate
 * <p>
 * Represents a mechanic in the workshop domain.
 * </p>
 */
@Entity
@Getter
public class Mechanic extends AuditableAbstractAggregateRoot<Mechanic> {

    @Embedded
    @Column(name = "profile_id")
    private ProfileId profileId;

    /**
     * Specializations assigned to the mechanic
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "mechanic_specializations", joinColumns = @JoinColumn(name = "mechanic_id"), inverseJoinColumns = @JoinColumn(name = "specialization_id"))
    private Set<Specialization> specializations = new HashSet<>();

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
     * 
     * @param profileId the profile id from Profiles BC
     */
    public Mechanic(Long profileId) {
        this();
        this.profileId = new ProfileId(profileId);
        this.yearsOfExperience = 0;
    }

    /**
     * Constructor with ProfileId value object
     * 
     * @param profileId the profile id value object
     * @see ProfileId
     */
    public Mechanic(ProfileId profileId) {
        this();
        this.profileId = profileId;
        this.yearsOfExperience = 0;
    }

    /**
     * Constructor with all fields
     * 
     * @param profileId         the profile id
     * @param specializations   the specializations of the mechanic
     * @param yearsOfExperience the years of experience
     */
    public Mechanic(Long profileId,
                    Set<Specialization> specializations,
                    Integer yearsOfExperience) {
        this();
        this.profileId = new ProfileId(profileId);
        this.yearsOfExperience = yearsOfExperience == null ? 0 : Math.max(0, yearsOfExperience);
        this.specializations = Specialization.validateSpecializations(specializations);
    }

    /**
     * Get profile id
     * 
     * @return the profile id from Profiles BC
     */
    public Long getProfileId() {
        return this.profileId.profileId();
    }

    /**
     * Get the names of specializations assigned to the mechanic.
     * 
     * @return Set of specialization names
     */
    public Set<String> getSpecializationNames() {
        return this.specializations.stream()
                .map(Specialization::getStringName)
                .collect(Collectors.toSet());
    }

    /**
     * Check if the mechanic has a specific specialization.
     * 
     * @param specializationName Name of the specialization to check
     * @return true if the mechanic has the specialization, false otherwise
     */
    public boolean hasSpecialization(String specializationName) {
        return this.specializations.stream()
                .anyMatch(specialization -> specialization.getStringName().equals(specializationName));
    }

    /**
     * Add a set of specializations to the mechanic.
     * 
     * @param specializations the specializations.
     * @return the mechanic.
     */
    public Mechanic addSpecializations(Set<Specialization> specializations) {
        var validatedSpecializations = Specialization.validateSpecializations(specializations);
        this.specializations.addAll(validatedSpecializations);
        return this;
    }

    /**
     * Add specializations from a list of strings
     * This method will be used by the command handler to convert from strings to
     * entities
     * 
     * @param specializationNames list of specialization names
     */
    public void addSpecializationsFromSet(Set<String> specializationNames) {
        if (specializationNames != null && !specializationNames.isEmpty()) {
            this.specializations.add(Specialization.getDefaultSpecialization());
        }
    }

    /**
     * Update mechanic specializations
     * 
     * @param specializations new specializations set
     */
    public void updateSpecializations(Set<Specialization> specializations) {
        this.specializations.clear();
        if (specializations != null) {
            this.specializations.addAll(specializations);
        }
    }

    /**
     * Update years of experience
     * 
     * @param yearsOfExperience new years of experience
     */
    public void updateYearsOfExperience(Integer yearsOfExperience) {
        if (yearsOfExperience != null && yearsOfExperience >= 0) {
            this.yearsOfExperience = yearsOfExperience;
        }
    }

    /**
     * Factory method to create a Mechanic from parameters
     * 
     * @param profileId         the profile id
     * @param specializations   the specializations of the mechanic
     * @param yearsOfExperience the years of experience
     * @return a new Mechanic instance
     */
    public static Mechanic create(CreateMechanicCommand command) {
    return new Mechanic(
        command.profileId(),
        command.specializations(),
        command.yearsOfExperience());
    }
}