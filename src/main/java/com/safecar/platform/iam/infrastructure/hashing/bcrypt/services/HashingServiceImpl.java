package com.safecar.platform.iam.infrastructure.hashing.bcrypt.services;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.safecar.platform.iam.infrastructure.hashing.bcrypt.BCryptHashingService;

/**
 * Implementation of the BCrypt hashing service.
 */
@Service
public class HashingServiceImpl implements BCryptHashingService {
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Default constructor.
     * <p>
     *  It initializes the password encoder.
     * </p>
     */
    public HashingServiceImpl() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // inherited javadoc
    @Override
    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    // inherited javadoc
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}