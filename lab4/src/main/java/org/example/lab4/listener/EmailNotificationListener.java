package org.example.lab4.listener;

import org.example.lab4.event.dto.AuditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationListener.class);

    @Autowired(required = false) // required=false для случая, если почта не настроена
    private JavaMailSender mailSender;

    private static final String ADMIN_EMAIL = "yegor.sibryayev@gmail.com";

    @JmsListener(destination = "notification.queue")
    public void processNotificationEvent(AuditEvent event) {
        try {
            logger.info("📧 Processing notification event: {}", event);

            if (shouldSendEmail(event)) {
                sendEmail(event);
                logger.info("✅ Email sent to: {}", ADMIN_EMAIL);
            } else {
                logger.info("⏭️ Skipping email for event: {}", event);
            }

        } catch (Exception e) {
            logger.error("❌ Failed to process notification event: {}", event, e);
        }
    }

    /**
     * Условия для отправки email:
     * 1. Удаление любой сущности
     * 2. Добавление автора
     * 3. Удаление книги
     */
    private boolean shouldSendEmail(AuditEvent event) {
        return "DELETED".equals(event.getEventType()) ||  // Любое удаление
                ("AUTHOR".equals(event.getEntityType()) &&
                        "CREATED".equals(event.getEventType())) || // Новый автор
                ("BOOK".equals(event.getEntityType()) &&
                        "DELETED".equals(event.getEventType()));   // Удаление книги
    }

    private void sendEmail(AuditEvent event) {
        if (mailSender == null) {
            logger.warn("⚠️ JavaMailSender not configured. Email will not be sent.");
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(ADMIN_EMAIL);  // Используется здесь
        message.setSubject(getEmailSubject(event));
        message.setText(getEmailBody(event));

        // Можно добавить отправителя (from)
        message.setFrom("yegor.sibryayev@mail.ru");

        try {
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("❌ Failed to send email: {}", e.getMessage());
        }
    }

    private String getEmailSubject(AuditEvent event) {
        return String.format("[Book Catalog] %s %s: %s",
                event.getEntityType(),
                event.getEventType(),
                event.getEntityId());
    }

    private String getEmailBody(AuditEvent event) {
        return String.format(
                "Event Type: %s\n" +
                        "Entity Type: %s\n" +
                        "Entity ID: %d\n" +
                        "Changed By: %s\n" +
                        "Timestamp: %s\n" +
                        "Event ID: %s\n\n" +
                        "Details: %s",
                event.getEventType(),
                event.getEntityType(),
                event.getEntityId(),
                event.getChangedBy(),
                event.getTimestamp(),
                event.getEventId(),
                event.getChangeDetails() != null ? event.getChangeDetails().toString() : "No details"
        );
    }
}