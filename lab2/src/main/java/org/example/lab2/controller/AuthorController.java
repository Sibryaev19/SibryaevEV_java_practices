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

    // ВСЁ на одной странице: список + форма
    @GetMapping
    public String authorsPage(Model model) {
        // 1. Список авторов для таблицы
        List<Author> authors = authorService.getAllAuthors();
        model.addAttribute("authors", authors);

        // 2. Пустой автор для формы добавления
        model.addAttribute("newAuthor", new Author());

        // 3. Автор для редактирования (если перешли с edit)
        model.addAttribute("editAuthor", new Author());

        return "authors";
    }

    // Добавить/обновить автора
    @PostMapping("/save")
    public String saveAuthor(@ModelAttribute("newAuthor") Author author) {
        authorService.createAuthor(author);
        return "redirect:/authors";
    }

    // Удалить автора
    @GetMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return "redirect:/authors";
    }
}