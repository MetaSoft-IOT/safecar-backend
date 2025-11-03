package com.safecar.platform.workshopOps.domain.exceptions;

/**
 * Exception thrown when attempting to create an appointment with a code that already exists.
 */
public class AppointmentAlreadyExistsException extends RuntimeException {

    public AppointmentAlreadyExistsException(String code) {
        super("An appointment with code " + code + " already exists");
    }
}

