package org.example.lab2.service;

import org.example.lab2.entity.Book;
import java.util.List;

public interface BookService {
    Book createBook(Book book);
    Book getBookById(Long id);
    List<Book> getAllBooks();
    Book updateBook(Book book);
    void deleteBook(Long id);
    List<Book> getBooksByAuthorId(Long authorId);
}