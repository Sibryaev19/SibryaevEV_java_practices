package org.example.lab2.controller;

import org.example.lab2.entity.Author;
import org.example.lab2.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping
    public String authorsPage(Model model) {
        // 1. Список авторов для таблицы
        List<Author> authors = authorService.getAllAuthors();
        model.addAttribute("authors", authors);

        // 2. По умолчанию - пустой автор для создания
        if (!model.containsAttribute("editAuthor")) {
            model.addAttribute("editAuthor", new Author());
        }

        return "authors";
    }

    @GetMapping("/edit/{id}")
    public String editAuthorPage(@PathVariable Long id, Model model) {
        // 1. Список авторов для таблицы
        List<Author> authors = authorService.getAllAuthors();
        model.addAttribute("authors", authors);

        // 2. Автор для редактирования
        Author author = authorService.getAuthorById(id);
        model.addAttribute("editAuthor", author);

        return "authors";
    }

    @PostMapping("/save")
    public String saveAuthor(@ModelAttribute Author author) {
        if (author.getId() == null) {
            authorService.createAuthor(author);
        } else {
            authorService.updateAuthor(author);
        }
        return "redirect:/authors";
    }

    @GetMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return "redirect:/authors";
    }
}