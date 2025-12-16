package org.example.lab4.service;

import org.example.lab4.dao.BookDao;
import org.example.lab4.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    @Override
    public Book createBook(Book book) {
        return bookDao.save(book);
    }

    @Override
    public Book getBookById(Long id) {
        return bookDao.findById(id);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.findAll();
    }

    @Override
    public Book updateBook(Book book) {
        return bookDao.update(book);
    }

    @Override
    public void deleteBook(Long id) {
        bookDao.delete(id);
    }

    @Override
    public List<Book> getBooksByAuthorId(Long authorId) {
        return bookDao.findByAuthorId(authorId);
    }
}