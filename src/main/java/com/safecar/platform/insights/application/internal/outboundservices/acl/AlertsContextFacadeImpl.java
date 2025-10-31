package com.safecar.platform.insights.application.internal.outboundservices.acl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AlertsContextFacadeImpl implements AlertsContextFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlertsContextFacadeImpl.class);

    @Override
    public void sendCoaching(Long driverId, Long vehicleId, String title, String content, int priority) {
        LOGGER.info(
            "Stub AlertsContextFacadeImpl.sendCoaching driver={} vehicle={} priority={} title={} content={}",
            driverId, vehicleId, priority, title, content
        );
        // TODO integrar con el contexto de Alerts cuando esté disponible
    }
}
