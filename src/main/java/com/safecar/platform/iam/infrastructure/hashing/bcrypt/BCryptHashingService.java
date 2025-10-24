package com.safecar.platform.iam.infrastructure.hashing.bcrypt;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.safecar.platform.iam.application.internal.outboundservices.hashing.HashingService;

/**
 * BCrypt hashing service.
 * <p>
 *     This service is responsible for hashing and verifying passwords using BCrypt algorithm.
 *     It extends {@link HashingService} and {@link PasswordEncoder}.
 * </p>
 */
public interface BCryptHashingService extends HashingService, PasswordEncoder {
}
