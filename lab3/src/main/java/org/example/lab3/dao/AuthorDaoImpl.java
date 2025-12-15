package org.example.lab3.dao;

import org.example.lab3.entity.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class AuthorDaoImpl implements AuthorDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Author save(Author author) {
        if (author.getId() == null) {
            entityManager.persist(author);
            return author;
        } else {
            // Вариант 1: Если хотите обновить существующего автора
            Author existing = entityManager.find(Author.class, author.getId());
            if (existing != null) {
                // Копируем поля из author в existing
                existing.setFio(author.getFio());
                existing.setNickname(author.getNickname());
                existing.setBirthDate(author.getBirthDate());
                existing.setDescription(author.getDescription());
                return existing; // entityManager.merge(existing) не нужен, так как existing уже управляемый
            } else {
                // Если автор с таким id не найден, создаем нового
                entityManager.persist(author);
                return author;
            }
        }
    }

    @Override
    public Author findById(Long id) {
        return entityManager.find(Author.class, id);
    }

    @Override
    public List<Author> findAll() {
        TypedQuery<Author> query = entityManager.createQuery(
                "SELECT a FROM Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public Author update(Author author) {
        return entityManager.merge(author);
    }

    @Override
    public void delete(Long id) {
        Author author = entityManager.find(Author.class, id);
        if (author != null) {
            entityManager.remove(author);
        }
    }
}