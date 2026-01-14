package org.example.lab4.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false)
    private String eventType; // "CREATED", "UPDATED", "DELETED"

    @Column(name = "entity_type", nullable = false)
    private String entityType; // "AUTHOR", "BOOK"

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(name = "changed_by")
    private String changedBy = "anonymous";

    @Column(name = "change_details", columnDefinition = "jsonb")
    private String changeDetails; // JSON строка

    @Column(name = "event_time")
    private LocalDateTime eventTime;

    // Конструкторы
    public AuditLogEntry() {
        this.eventTime = LocalDateTime.now();
    }

    public AuditLogEntry(String eventType, String entityType, Long entityId) {
        this();
        this.eventType = eventType;
        this.entityType = entityType;
        this.entityId = entityId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getChangeDetails() {
        return changeDetails;
    }

    public void setChangeDetails(String changeDetails) {
        this.changeDetails = changeDetails;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public String toString() {
        return "AuditLogEntry{" +
                "id=" + id +
                ", eventType='" + eventType + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entityId=" + entityId +
                ", eventTime=" + eventTime +
                '}';
    }
}