package com.obs.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class Base {
    // Auto set the value to current timestamp in DB column definition
    // Value will be inserted upon transaction commit
    @Column(columnDefinition = "TIMESTAMP", updatable = false)
    protected LocalDateTime createdDateTime;
    @Column(updatable = false)
    protected Long createdBy;
    // Auto update the value to current timestamp in DB column definition
    // Value will be inserted upon transaction commit
    @Column(columnDefinition = "TIMESTAMP", updatable = false)
    protected LocalDateTime updatedDateTime;
    protected Long updatedBy;

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public Base setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Base setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public Base setUpdatedDateTime(LocalDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
        return this;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public Base setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    @Override
    public String toString() {
        return "Base{" +
                "createdDateTime=" + createdDateTime +
                ", createdBy=" + createdBy +
                ", updatedDateTime=" + updatedDateTime +
                ", updatedBy=" + updatedBy +
                '}';
    }
}
