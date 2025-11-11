package com.safecar.platform.workshop.interfaces.rest.transform;

import com.safecar.platform.workshop.domain.model.commands.CloseServiceOrderCommand;
import com.safecar.platform.workshop.interfaces.rest.resources.CloseServiceOrderResource;

/**
 * Assembles a {@link CloseServiceOrderCommand} from a
 * {@link CloseServiceOrderResource}.
 */
public class CloseServiceOrderCommandFromResourceAssembler {

    public static CloseServiceOrderCommand toCommandFromResource(CloseServiceOrderResource resource) {
        return new CloseServiceOrderCommand(
                resource.serviceOrderId(),
                resource.reason(),
                resource.notes());
    }

}
