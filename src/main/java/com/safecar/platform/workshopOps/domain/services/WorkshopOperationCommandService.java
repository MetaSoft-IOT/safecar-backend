package com.safecar.platform.workshopOps.domain.services;

import com.safecar.platform.workshopOps.domain.model.commands.AllocateServiceBayCommand;

public interface WorkshopOperationCommandService {
    void handle(AllocateServiceBayCommand command);
}
