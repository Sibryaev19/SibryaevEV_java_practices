package org.example.lab4.dao;

import org.example.lab4.entity.Author;
import java.util.List;

public interface AuthorDao {
    Author save(Author author);
    Author findById(Long id);
    List<Author> findAll();
    Author update(Author author);
    void delete(Long id);
}