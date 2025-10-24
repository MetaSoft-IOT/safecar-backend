package com.safecar.platform.shared.domain.model.entities;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

/**
 * Abstract base class for entities that require auditing.
 * <p>
 * Provides automatic management of creation and update timestamps.
 */
@MappedSuperclass
// Use Spring Data JPA's AuditingEntityListener to populate audit fields
@EntityListeners(AuditingEntityListener.class)
public class AuditableModel {
    /**
     * The timestamp when the entity was created.
     * Set automatically and not updatable.
     */
    @Getter
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date createdAt;
    
    /**
     * The timestamp when the entity was last updated.
     * Set automatically.
     */
    @Getter
    @LastModifiedDate
    @Column(nullable = false)
    private Date updatedAt;
}
