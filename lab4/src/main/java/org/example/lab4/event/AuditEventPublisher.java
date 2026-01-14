package org.example.lab4.event;

import org.example.lab4.event.dto.AuditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import jakarta.jms.Queue;

@Service
public class AuditEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(AuditEventPublisher.class);

    @Autowired
    private JmsTemplate jmsTemplate;


    /**
     * Отправляет событие в очередь аудита
     */
    public void publishAuditEvent(AuditEvent event) {
        try {
            jmsTemplate.convertAndSend("audit.queue", event);
            logger.info("✅ Audit event sent to queue: {}", event);
        } catch (Exception e) {
            logger.error("❌ Failed to send audit event: {}", event, e);
        }
    }

    /**
     * Отправляет событие в очередь уведомлений
     */
    public void publishNotificationEvent(AuditEvent event) {
        try {
            jmsTemplate.convertAndSend("notification.queue", event);
            logger.info("✅ Notification event sent to queue: {}", event);
        } catch (Exception e) {
            logger.error("❌ Failed to send notification event: {}", event, e);
        }
    }

    /**
     * Комбинированный метод: отправляет и в аудит, и в уведомления
     * если событие соответствует условиям для email
     */
    public void publishEvent(AuditEvent event) {
        publishAuditEvent(event);

        // Проверяем условия для отправки email
        if (shouldNotify(event)) {
            publishNotificationEvent(event);
        }
    }

    /**
     * Условия для отправки email:
     * 1. Удаление любой сущности
     * 2. Добавление автора
     * 3. Удаление книги
     */
    private boolean shouldNotify(AuditEvent event) {
        return "DELETED".equals(event.getEventType()) ||  // Любое удаление
                ("AUTHOR".equals(event.getEntityType()) &&
                        "CREATED".equals(event.getEventType())) || // Новый автор
                ("BOOK".equals(event.getEntityType()) &&
                        "DELETED".equals(event.getEventType()));   // Удаление книги
    }
}