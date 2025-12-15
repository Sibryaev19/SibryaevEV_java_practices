package org.example.lab3.dao;

import org.example.lab3.entity.Author;
import java.util.List;

public interface AuthorDao {
    Author save(Author author);
    Author findById(Long id);
    List<Author> findAll();
    Author update(Author author);
    void delete(Long id);
}