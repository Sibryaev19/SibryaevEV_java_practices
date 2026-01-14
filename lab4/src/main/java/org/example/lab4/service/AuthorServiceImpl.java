package org.example.lab4.service;

import org.example.lab4.dao.AuthorDao;
import org.example.lab4.entity.Author;
import org.example.lab4.event.AuditEventPublisher;
import org.example.lab4.event.util.AuditEventFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private AuditEventPublisher auditEventPublisher;

    @Override
    public Author createAuthor(Author author) {
        Author savedAuthor = authorDao.save(author);

        // Отправляем событие о создании автора
        auditEventPublisher.publishEvent(
                AuditEventFactory.createAuthorCreatedEvent(savedAuthor)
        );

        return savedAuthor;
    }

    @Override
    public Author getAuthorById(Long id) {
        return authorDao.findById(id);
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorDao.findAll();
    }

    @Override
    public Author updateAuthor(Author author) {
        // Получаем старую версию автора для аудита
        Author oldAuthor = authorDao.findById(author.getId());
        if (oldAuthor == null) {
            return null;
        }

        // Сохраняем копию старого автора
        Author oldAuthorCopy = new Author();
        oldAuthorCopy.setId(oldAuthor.getId());
        oldAuthorCopy.setFio(oldAuthor.getFio());
        oldAuthorCopy.setNickname(oldAuthor.getNickname());
        oldAuthorCopy.setBirthDate(oldAuthor.getBirthDate());
        oldAuthorCopy.setDescription(oldAuthor.getDescription());

        // Обновляем автора
        Author updatedAuthor = authorDao.update(author);

        // Отправляем событие об обновлении
        auditEventPublisher.publishEvent(
                AuditEventFactory.createAuthorUpdatedEvent(oldAuthorCopy, updatedAuthor)
        );

        return updatedAuthor;
    }

    @Override
    public void deleteAuthor(Long id) {
        // Получаем автора перед удалением для аудита
        Author author = authorDao.findById(id);
        if (author != null) {
            // Удаляем автора
            authorDao.delete(id);

            // Отправляем событие об удалении
            auditEventPublisher.publishEvent(
                    AuditEventFactory.createAuthorDeletedEvent(author)
            );
        }
    }
}