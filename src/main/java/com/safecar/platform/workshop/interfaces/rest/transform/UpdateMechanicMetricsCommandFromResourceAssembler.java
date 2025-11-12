package com.safecar.platform.workshop.interfaces.rest.transform;

import com.safecar.platform.workshop.domain.model.commands.UpdateMechanicMetricsCommand;
import com.safecar.platform.workshop.interfaces.rest.resources.UpdateMechanicMetricsResource;

/**
 * Update Mechanic Metrics Command From Resource Assembler
 * <p>
 * Transforms REST resources into domain commands for clean separation of
 * concerns.
 * </p>
 */
public class UpdateMechanicMetricsCommandFromResourceAssembler {

    /**
     * Transforms the {@link UpdateMechanicMetricsResource} into a
     * {@link UpdateMechanicMetricsCommand}
     * 
     * @param resource the resource to transform
     * @return the transformed command
     */
    public static UpdateMechanicMetricsCommand toCommandFromResource(UpdateMechanicMetricsResource resource) {
        var specializations = SpecializationSetFromStringAssembler
                .toSpecializationSetFromStringSet(resource.specializations());

    return new UpdateMechanicMetricsCommand(
        specializations,
        resource.yearsOfExperience());
    }
}
