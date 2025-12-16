package org.example.lab4.service;

import org.example.lab4.entity.Book;
import java.util.List;

public interface BookService {
    Book createBook(Book book);
    Book getBookById(Long id);
    List<Book> getAllBooks();
    Book updateBook(Book book);
    void deleteBook(Long id);
    List<Book> getBooksByAuthorId(Long authorId);
}