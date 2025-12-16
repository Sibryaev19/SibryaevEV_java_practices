package org.example.lab4.service;

import org.example.lab4.dao.AuthorDao;
import org.example.lab4.entity.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorDao authorDao;

    @Override
    public Author createAuthor(Author author) {
        return authorDao.save(author);
    }

    @Override
    public Author getAuthorById(Long id) {
        return authorDao.findById(id);
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorDao.findAll();
    }

    @Override
    public Author updateAuthor(Author author) {
        return authorDao.update(author);
    }

    @Override
    public void deleteAuthor(Long id) {
        authorDao.delete(id);
    }
}