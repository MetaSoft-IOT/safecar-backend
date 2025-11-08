package com.safecar.platform.workshop.interfaces.rest.resources;

import java.util.Set;

/**
 * Mechanic Resource
 * <p>
 * REST resource representation of a Mechanic within the Workshop bounded
 * context.
 * </p>
 * 
 * @param companyName       the name of the workshop company
 * @param specializations   set of specializations of the mechanic
 * @param yearsOfExperience years of professional experience
 */
public record MechanicResource(
        String companyName,
        Set<String> specializations,
        Integer yearsOfExperience) {

}
