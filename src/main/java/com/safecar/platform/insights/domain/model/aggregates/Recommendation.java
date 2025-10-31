package com.safecar.platform.insights.domain.model.aggregates;

import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Entity
@Table(name = "recommendations", indexes = {
  @Index(name="idx_reco_driver", columnList = "driver_id"),
  @Index(name="idx_reco_vehicle", columnList = "vehicle_id"),
  @Index(name="idx_reco_status", columnList = "status")
})
public class Recommendation extends AuditableAbstractAggregateRoot<Recommendation> {

  public enum Status { DRAFT, PUBLISHED, SENT }

  @NotNull @Column(name = "driver_id", nullable = false)
  private Long driverId;

  @NotNull @Column(name = "vehicle_id", nullable = false)
  private Long vehicleId;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "content", nullable = false, length = 4000)
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private Status status = Status.DRAFT;

  protected Recommendation() {}

  public Recommendation(Long driverId, Long vehicleId, String title, String content) {
    this.driverId = driverId;
    this.vehicleId = vehicleId;
    this.title = title;
    this.content = content;
  }

  public void publish() { this.status = Status.PUBLISHED; }
  public void markSent() { this.status = Status.SENT; }
}
