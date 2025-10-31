package com.safecar.platform.profiles.domain.model.aggregates;



import com.safecar.platform.profiles.domain.model.commands.CreateWorkshopCommand;
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

import java.util.UUID;

@Setter
@Getter
@Entity
public class Workshop extends AuditableAbstractAggregateRoot<Workshop> {
    @NotBlank
    private String fullName;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    @Embedded
    private Phone phone;

    @NotBlank
    private String companyName;

    @Embedded
    private Dni dni;

    @NotNull(message = "UserId cannot be null")
    @Positive(message = "UserId must be positive")
    private Long userId;

    public Workshop(CreateWorkshopCommand command, Long userId) {
        this.fullName = command.fullName();
        this.city = command.city();
        this.country = command.country();
        this.phone = new Phone(command.phone());;
        this.companyName = command.companyName();
        this.dni = new Dni(command.dni());
        this.userId = userId;
    }

    public Workshop() {}

    public String getPhone(){
        return phone.phone();
    }

    public String getDni(){
        return dni.dni();
    }
}
