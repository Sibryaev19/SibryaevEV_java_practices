package org.example.lab4.event.util;

import org.example.lab4.entity.Author;
import org.example.lab4.entity.Book;
import org.example.lab4.event.dto.AuditEvent;

import java.util.HashMap;
import java.util.Map;

public class AuditEventFactory {

    // Событие для создания автора
    public static AuditEvent createAuthorCreatedEvent(Author author) {
        AuditEvent event = new AuditEvent("CREATED", "AUTHOR", author.getId());

        Map<String, Object> details = new HashMap<>();
        details.put("author", convertAuthorToMap(author));
        event.setChangeDetails(details);

        return event;
    }

    // Событие для обновления автора
    public static AuditEvent createAuthorUpdatedEvent(Author oldAuthor, Author newAuthor) {
        AuditEvent event = new AuditEvent("UPDATED", "AUTHOR", newAuthor.getId());

        Map<String, Object> details = new HashMap<>();
        details.put("old", convertAuthorToMap(oldAuthor));
        details.put("new", convertAuthorToMap(newAuthor));
        event.setChangeDetails(details);

        return event;
    }

    // Событие для удаления автора
    public static AuditEvent createAuthorDeletedEvent(Author author) {
        AuditEvent event = new AuditEvent("DELETED", "AUTHOR", author.getId());

        Map<String, Object> details = new HashMap<>();
        details.put("author", convertAuthorToMap(author));
        event.setChangeDetails(details);

        return event;
    }

    // Событие для создания книги
    public static AuditEvent createBookCreatedEvent(Book book) {
        AuditEvent event = new AuditEvent("CREATED", "BOOK", book.getId());

        Map<String, Object> details = new HashMap<>();
        details.put("book", convertBookToMap(book));
        event.setChangeDetails(details);

        return event;
    }

    // Событие для обновления книги
    public static AuditEvent createBookUpdatedEvent(Book oldBook, Book newBook) {
        AuditEvent event = new AuditEvent("UPDATED", "BOOK", newBook.getId());

        Map<String, Object> details = new HashMap<>();
        details.put("old", convertBookToMap(oldBook));
        details.put("new", convertBookToMap(newBook));
        event.setChangeDetails(details);

        return event;
    }

    // Событие для удаления книги
    public static AuditEvent createBookDeletedEvent(Book book) {
        AuditEvent event = new AuditEvent("DELETED", "BOOK", book.getId());

        Map<String, Object> details = new HashMap<>();
        details.put("book", convertBookToMap(book));
        event.setChangeDetails(details);

        return event;
    }

    private static Map<String, Object> convertAuthorToMap(Author author) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", author.getId());
        map.put("fio", author.getFio());
        map.put("nickname", author.getNickname());
        map.put("birthDate", author.getBirthDate());
        map.put("description", author.getDescription());
        return map;
    }

    private static Map<String, Object> convertBookToMap(Book book) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", book.getId());
        map.put("name", book.getName());
        map.put("description", book.getDescription());
        map.put("publicationYear", book.getPublicationYear());
        map.put("pageNum", book.getPageNum());
        map.put("rating", book.getRating());
        map.put("authorId", book.getAuthor() != null ? book.getAuthor().getId() : null);
        map.put("authorName", book.getAuthor() != null ? book.getAuthor().getFio() : null);
        return map;
    }
}