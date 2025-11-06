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
 * PersonProfile aggregate — a generic person representation stored in the Profiles BC.
 */
@Setter
@Getter
@Entity
public class PersonProfile extends AuditableAbstractAggregateRoot<PersonProfile> {

    @NotNull
    @Positive
    private Long userId;

    @NotBlank
    private String fullName;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    @Embedded
    private Phone phone;

    @Embedded
    private Dni dni;

    protected PersonProfile() {
    }

    public PersonProfile(CreatePersonProfileCommand command, Long userId) {
        this.userId = userId;
        this.fullName = command.fullName();
        this.city = command.city();
        this.country = command.country();
        this.phone = new Phone(command.phone());
        this.dni = new Dni(command.dni());
    }

}
