package com.safecar.platform.workshopOps.application.internal.commandservices;

import org.springframework.stereotype.Service;

import com.safecar.platform.workshopOps.domain.model.aggregates.WorkshopOperation;
import com.safecar.platform.workshopOps.domain.model.commands.AllocateServiceBayCommand;
import com.safecar.platform.workshopOps.infrastructure.persistence.jpa.repositories.ServiceBayRepository;
import com.safecar.platform.workshopOps.infrastructure.persistence.jpa.repositories.WorkshopOperationRepository;
import com.safecar.platform.workshopOps.domain.services.WorkshopOperationCommandService;

@Service
public class WorkshopOperationCommandServiceImpl implements WorkshopOperationCommandService {

    private final WorkshopOperationRepository operationRepository;
    private final ServiceBayRepository serviceBayRepository;

    public WorkshopOperationCommandServiceImpl(WorkshopOperationRepository operationRepository, ServiceBayRepository serviceBayRepository) {
        this.operationRepository = operationRepository;
        this.serviceBayRepository = serviceBayRepository;
    }

    @Override
    public void handle(AllocateServiceBayCommand command) {
        var maybeOp = operationRepository.findByWorkshop(command.workshopId());
        WorkshopOperation op;
        if (maybeOp.isEmpty()) {
            op = new WorkshopOperation(command.workshopId(), 0, 0);
        } else {
            op = maybeOp.get();
        }

        var bay = op.allocateBay(command.label());
        operationRepository.save(op);
        // bay is persisted because cascade on WorkshopOperation.bays
    }
}
