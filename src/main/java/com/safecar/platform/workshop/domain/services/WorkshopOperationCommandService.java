package com.safecar.platform.workshop.domain.services;

import com.safecar.platform.workshop.domain.model.commands.AllocateServiceBayCommand;

public interface WorkshopOperationCommandService {
    void handle(AllocateServiceBayCommand command);
}
