package com.safecar.platform.insights.application.internal.outboundservices.acl;

public interface AlertsContextFacade {
  void sendCoaching(Long driverId, Long vehicleId, String title, String content, int priority);
}
