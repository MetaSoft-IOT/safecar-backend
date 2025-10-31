package com.safecar.platform.iam.application.internal.commandservices;

import com.safecar.platform.iam.application.internal.outboundservices.acl.ExternalProfileService;
import com.safecar.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.safecar.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.safecar.platform.iam.domain.model.aggregates.User;
import com.safecar.platform.iam.domain.model.commands.SignInCommand;
import com.safecar.platform.iam.domain.model.commands.SignUpDriverCommand;
import com.safecar.platform.iam.domain.model.commands.SignUpWorkshopCommand;
import com.safecar.platform.iam.domain.model.entities.Role;
import com.safecar.platform.iam.domain.model.valueobjects.Roles;
import com.safecar.platform.iam.domain.services.UserCommandService;
import com.safecar.platform.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.safecar.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final ExternalProfileService externalProfileService;

    public UserCommandServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                                  HashingService hashingService, TokenService tokenService,
                                  ExternalProfileService externalProfileService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.externalProfileService = externalProfileService;
    }

    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        var user = userRepository.findByEmail(command.email());
        if (user.isEmpty())
            throw new RuntimeException("User not found");
        if (!hashingService.matches(command.password(), user.get().getPassword()))
            throw new RuntimeException("Invalid password");
        var token = tokenService.generateToken(user.get().getEmail());
        return Optional.of(ImmutablePair.of(user.get(), token));
    }

    @Override
    @Transactional
    public Optional<User> handle(SignUpDriverCommand command) {
        if (userRepository.existsByEmail(command.email()))
            throw new RuntimeException("Email already exists");

        // Buscar el rol de desarrollador en el repositorio de roles
        Role driverRole = roleRepository.findByName(Roles.valueOf("ROLE_DRIVER"))
                .orElseThrow(() -> new RuntimeException("Driver role not found"));

        // Crear una lista con el rol de desarrollador
        List<Role> roles = List.of(driverRole);

        // Crear el usuario con el rol de driver
        var user = new User(command.email(), hashingService.encode(command.password()), roles);
        userRepository.save(user);

        Long driverId = externalProfileService.createDriver(
                command.fullName(), command.city(), command.country(),
                command.phone(), command.dni(), user.getId()
        );

        if (driverId == 0L) {
            throw new RuntimeException("Failed to create Driver profile");
        }

        return Optional.of(user);
    }

    @Override
    @Transactional
    public Optional<User> handle(SignUpWorkshopCommand command) {
        if (userRepository.existsByEmail(command.email()))
            throw new RuntimeException("Email already exists");

        // Buscar el rol de desarrollador en el repositorio de roles
        Role workShopRole = roleRepository.findByName(Roles.valueOf("ROLE_WORKSHOP"))
                .orElseThrow(() -> new RuntimeException("Workshop role not found"));

        // Crear una lista con el rol de desarrollador
        List<Role> roles = List.of(workShopRole);

        // Crear el usuario con el rol de Workshop
        var user = new User(command.email(), hashingService.encode(command.password()), roles);
        userRepository.save(user);

        Long workshopId = externalProfileService.createWorkshop(
                command.fullName(), command.city(), command.country(),
                command.phone(), command.companyName(), command.ruc(), user.getId()
        );

        if (workshopId == 0L) {
            throw new RuntimeException("Failed to create WorkShop profile");
        }

        return Optional.of(user);
    }
}
