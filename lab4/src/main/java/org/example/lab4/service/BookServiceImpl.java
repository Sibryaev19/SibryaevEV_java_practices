package org.example.lab4.service;

import org.example.lab4.dao.AuthorDao;
import org.example.lab4.dao.BookDao;
import org.example.lab4.entity.Author;
import org.example.lab4.entity.Book;
import org.example.lab4.event.AuditEventPublisher;
import org.example.lab4.event.util.AuditEventFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    @Autowired
    private AuthorDao authorDao; // Добавляем для проверки автора

    @Autowired
    private AuditEventPublisher auditEventPublisher; // Добавляем publisher

    @Override
    public Book createBook(Book book) {
        // Проверяем существование автора
        Author author = authorDao.findById(book.getAuthor().getId());
        if (author == null) {
            return null; // или выбросить исключение
        }

        book.setAuthor(author);
        Book savedBook = bookDao.save(book);

        // Отправляем событие о создании книги
        auditEventPublisher.publishEvent(
                AuditEventFactory.createBookCreatedEvent(savedBook)
        );

        return savedBook;
    }

    @Override
    public Book getBookById(Long id) {
        return bookDao.findById(id);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.findAll();
    }

    @Override
    public Book updateBook(Book book) {
        // Получаем старую версию книги для аудита
        Book oldBook = bookDao.findById(book.getId());
        if (oldBook == null) {
            return null;
        }

        // Сохраняем копию старой книги
        Book oldBookCopy = new Book();
        oldBookCopy.setId(oldBook.getId());
        oldBookCopy.setName(oldBook.getName());
        oldBookCopy.setDescription(oldBook.getDescription());
        oldBookCopy.setPublicationYear(oldBook.getPublicationYear());
        oldBookCopy.setPageNum(oldBook.getPageNum());
        oldBookCopy.setRating(oldBook.getRating());
        oldBookCopy.setAuthor(oldBook.getAuthor());

        // Обновляем книгу
        Book updatedBook = bookDao.update(book);

        // Отправляем событие об обновлении
        auditEventPublisher.publishEvent(
                AuditEventFactory.createBookUpdatedEvent(oldBookCopy, updatedBook)
        );

        return updatedBook;
    }

    @Override
    public void deleteBook(Long id) {
        // Получаем книгу перед удалением для аудита
        Book book = bookDao.findById(id);
        if (book != null) {
            // Удаляем книгу
            bookDao.delete(id);

            // Отправляем событие об удалении
            auditEventPublisher.publishEvent(
                    AuditEventFactory.createBookDeletedEvent(book)
            );
        }
    }

    @Override
    public List<Book> getBooksByAuthorId(Long authorId) {
        return bookDao.findByAuthorId(authorId);
    }
}