package org.example.lab4.service;

import org.example.lab4.entity.Author;
import java.util.List;

public interface AuthorService {
    Author createAuthor(Author author);
    Author getAuthorById(Long id);
    List<Author> getAllAuthors();
    Author updateAuthor(Author author);
    void deleteAuthor(Long id);
}