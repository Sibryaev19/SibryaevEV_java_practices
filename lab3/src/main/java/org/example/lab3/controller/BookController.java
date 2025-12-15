package org.example.lab3.controller;

import org.example.lab3.entity.Author;
import org.example.lab3.entity.Book;
import org.example.lab3.service.AuthorService;
import org.example.lab3.service.BookService;
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

    @GetMapping
    public String booksPage(Model model) {
        // 1. Список книг для таблицы
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);

        // 2. Список авторов для выпадающего списка
        List<Author> authors = authorService.getAllAuthors();
        model.addAttribute("authors", authors);

        // 3. По умолчанию - пустая книга для создания
        if (!model.containsAttribute("editBook")) {
            model.addAttribute("editBook", new Book());
        }

        return "books";
    }

    @GetMapping("/edit/{id}")
    public String editBookPage(@PathVariable Long id, Model model) {
        // 1. Список книг для таблицы
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);

        // 2. Список авторов для выпадающего списка
        List<Author> authors = authorService.getAllAuthors();
        model.addAttribute("authors", authors);

        // 3. Книга для редактирования
        Book book = bookService.getBookById(id);
        model.addAttribute("editBook", book);

        return "books";
    }

    @PostMapping("/save")
    public String saveBook(@ModelAttribute Book book) {
        if (book.getId() == null) {
            bookService.createBook(book);
        } else {
            bookService.updateBook(book);
        }
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}