package com.safecar.platform.workshopOps.application.internal.outboundservices.acl;

import com.safecar.platform.iam.interfaces.acl.IamContextFacade;
import com.safecar.platform.workshopOps.domain.model.valueobjects.DriverId;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * External IAM Service
 * <p>
 *     This service provides access to IAM bounded context functionality from WorkshopOps.
 *     It acts as an adapter/wrapper around the IamContextFacade to provide domain-specific
 *     operations for the WorkshopOps bounded context.
 * </p>
 */
@Service
public class ExternalIamService {
    private final IamContextFacade iamContextFacade;

    /**
     * Constructor
     *
     * @param iamContextFacade IAM Context Facade
     */
    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    /**
     * Fetch Driver By Email
     * <p>
     *     Retrieves driver information by email address and maps it to WorkshopOps domain objects.
     * </p>
     * @param email The driver's email address
     * @return An {@link Optional} of {@link DriverId} if found
     */
    public Optional<DriverId> fetchDriverByEmail(String email) {
        var userId = iamContextFacade.fetchUserIdByEmail(email);
        if (userId == null || userId <= 0L) {
            return Optional.empty();
        }
        
        var userEmail = iamContextFacade.fetchUserEmailByUserId(userId);
        if (userEmail == null || userEmail.isBlank()) {
            return Optional.empty();
        }
        
        // TODO: Fetch actual user full name when available in IAM
        var fullName = "Driver " + userId; // Fallback name until IAM provides full name
        
        return Optional.of(new DriverId(userId, fullName));
    }

    /**
     * Validate Driver Exists
     * <p>
     *     Validates if a driver exists in the IAM system by their ID.
     * </p>
     * @param driverId The driver's ID to validate
     * @return true if the driver exists, false otherwise
     */
    public boolean validateDriverExists(Long driverId) {
        return iamContextFacade.validateUserExists(driverId);
    }

    /**
     * Validate Driver Exists By Email
     * <p>
     *     Validates if a driver exists in the IAM system by their email address.
     * </p>
     * @param email The driver's email address to validate
     * @return true if the driver exists, false otherwise
     */
    public boolean validateDriverExistsByEmail(String email) {
        return iamContextFacade.validateUserExistsByEmail(email);
    }

    /**
     * Fetch Driver Email By ID
     * <p>
     *     Retrieves the email address for a given driver ID.
     * </p>
     * @param driverId The driver's ID
     * @return The driver's email address, or null if not found
     */
    public String fetchDriverEmailById(Long driverId) {
        return iamContextFacade.fetchUserEmailByUserId(driverId);
    }
}