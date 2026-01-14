package org.example.lab4.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.lab4.event.dto.AuditEvent;
import org.example.lab4.model.AuditLogEntry;
import org.example.lab4.repository.AuditLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class AuditLogListener {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogListener.class);

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @JmsListener(destination = "audit.queue")
    public void processAuditEvent(AuditEvent event) {
        try {
            logger.info("📝 Processing audit event: {}", event);

            // Конвертируем AuditEvent в AuditLogEntry
            AuditLogEntry logEntry = new AuditLogEntry();
            logEntry.setEventType(event.getEventType());
            logEntry.setEntityType(event.getEntityType());
            logEntry.setEntityId(event.getEntityId());
            logEntry.setChangedBy(event.getChangedBy());
            logEntry.setEventTime(event.getTimestamp());

            // Конвертируем changeDetails в JSON строку
            if (event.getChangeDetails() != null) {
                logEntry.setChangeDetails(objectMapper.writeValueAsString(event.getChangeDetails()));
            }

            // Сохраняем в БД
            auditLogRepository.save(logEntry);

            logger.info("✅ Audit log saved with ID: {}", logEntry.getId());

        } catch (Exception e) {
            logger.error("❌ Failed to process audit event: {}", event, e);
        }
    }
}