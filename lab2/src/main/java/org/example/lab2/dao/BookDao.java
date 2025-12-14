package org.example.lab2.dao;

import org.example.lab2.entity.Book;
import java.util.List;

public interface BookDao {
    Book save(Book book);
    Book findById(Long id);
    List<Book> findAll();
    Book update(Book book);
    void delete(Long id);
    List<Book> findByAuthorId(Long authorId);
}