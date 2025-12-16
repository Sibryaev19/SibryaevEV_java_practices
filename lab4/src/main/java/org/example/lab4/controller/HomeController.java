package org.example.lab4.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HomeController {

    @GetMapping
    public Map<String, Object> apiInfo() {
        Map<String, Object> apiInfo = new HashMap<>();

        Map<String, String> authors = new HashMap<>();
        authors.put("GET_all", "/api/authors");
        authors.put("GET_by_id", "/api/authors/{id}");
        authors.put("POST_create", "/api/authors");
        authors.put("PUT_update", "/api/authors/{id}");
        authors.put("DELETE", "/api/authors/{id}");

        Map<String, String> books = new HashMap<>();
        books.put("GET_all", "/api/books");
        books.put("GET_by_id", "/api/books/{id}");
        books.put("GET_by_author", "/api/books/author/{authorId}");
        books.put("POST_create", "/api/books");
        books.put("PUT_update", "/api/books/{id}");
        books.put("DELETE", "/api/books/{id}");

        apiInfo.put("authors", authors);
        apiInfo.put("books", books);
        apiInfo.put("content_negotiation", "Use Accept: application/json or application/xml header");

        return apiInfo;
    }
}