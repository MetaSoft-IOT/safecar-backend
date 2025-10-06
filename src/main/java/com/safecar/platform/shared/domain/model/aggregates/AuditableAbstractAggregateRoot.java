package com.safecar.platform.shared.domain.model.aggregates;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;

import lombok.Getter;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

/**
 * Abstract base class for aggregate roots that includes auditing fields.
 * <p>
 * Provides automatic management of creation and update timestamps,
 * as well as a generated primary key.
 * </p>
 * 
 * @param <T> the type of the aggregate root.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditableAbstractAggregateRoot.class)
public class AuditableAbstractAggregateRoot<T extends AbstractAggregateRoot<T>> extends AbstractAggregateRoot<T> {
     /**
     * The unique identifier for the aggregate root.
     */
    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    /**
     * The timestamp when the entity was created.
     * Set automatically and not updatable.
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    /**
     * The timestamp when the entity was last updated.
     * Set automatically.
     */
    @LastModifiedDate
    @Column(nullable = false)
    private Date updatedAt;
}
