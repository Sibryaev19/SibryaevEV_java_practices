package org.example.lab4.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditEvent {

    private String eventId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private String eventType;     // "CREATED", "UPDATED", "DELETED"
    private String entityType;    // "AUTHOR", "BOOK"
    private Long entityId;

    private String changedBy = "SYSTEM";

    private Map<String, Object> changeDetails;

    // Конструкторы
    public AuditEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }

    public AuditEvent(String eventType, String entityType, Long entityId) {
        this();
        this.eventType = eventType;
        this.entityType = entityType;
        this.entityId = entityId;
    }

    // Getters and Setters
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
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

    public Map<String, Object> getChangeDetails() {
        return changeDetails;
    }

    public void setChangeDetails(Map<String, Object> changeDetails) {
        this.changeDetails = changeDetails;
    }

    @Override
    public String toString() {
        return "AuditEvent{" +
                "eventId='" + eventId + '\'' +
                ", timestamp=" + timestamp +
                ", eventType='" + eventType + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entityId=" + entityId +
                '}';
    }
}