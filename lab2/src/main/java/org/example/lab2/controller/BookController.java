package org.example.lab2.controller;

import org.example.lab2.entity.Author;
import org.example.lab2.entity.Book;
import org.example.lab2.service.AuthorService;
import org.example.lab2.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    // ВСЁ на одной странице: список + форма
    @GetMapping
    public String booksPage(Model model) {
        // 1. Список книг для таблицы
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);

        // 2. Пустая книга для формы добавления
        Book newBook = new Book();
        model.addAttribute("newBook", newBook);

        // 3. Список авторов для выпадающего списка
        List<Author> authors = authorService.getAllAuthors();
        model.addAttribute("authors", authors);

        return "books";
    }

    // Добавить книгу
    @PostMapping("/save")
    public String saveBook(@ModelAttribute("newBook") Book book) {
        bookService.createBook(book);
        return "redirect:/books";
    }

    // Удалить книгу
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}