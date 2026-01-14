package org.example.lab4.repository;

import org.example.lab4.model.AuditLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLogEntry, Long> {
    // Можно добавить кастомные методы при необходимости
    // List<AuditLogEntry> findByEntityTypeAndEntityId(String entityType, Long entityId);
}