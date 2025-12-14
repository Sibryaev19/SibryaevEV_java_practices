package org.example.lab2.dao;

import org.example.lab2.entity.Author;
import java.util.List;

public interface AuthorDao {
    Author save(Author author);
    Author findById(Long id);
    List<Author> findAll();
    Author update(Author author);
    void delete(Long id);
}