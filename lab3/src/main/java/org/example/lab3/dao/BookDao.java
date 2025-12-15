package org.example.lab3.dao;

import org.example.lab3.entity.Book;
import java.util.List;

public interface BookDao {
    Book save(Book book);
    Book findById(Long id);
    List<Book> findAll();
    Book update(Book book);
    void delete(Long id);
    List<Book> findByAuthorId(Long authorId);
}