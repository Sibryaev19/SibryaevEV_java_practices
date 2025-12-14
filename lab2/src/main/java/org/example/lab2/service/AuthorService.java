package org.example.lab2.service;

import org.example.lab2.entity.Author;
import java.util.List;

public interface AuthorService {
    Author createAuthor(Author author);
    Author getAuthorById(Long id);
    List<Author> getAllAuthors();
    Author updateAuthor(Author author);
    void deleteAuthor(Long id);
}