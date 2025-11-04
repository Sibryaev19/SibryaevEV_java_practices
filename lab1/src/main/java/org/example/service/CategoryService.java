package org.example.service;

import org.example.entity.Category;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class CategoryService {

    @PersistenceContext
    private EntityManager entityManager;

    // Получить все категории
    public List<Category> getAllCategories() {
        return entityManager.createQuery("SELECT c FROM Category c", Category.class)
                .getResultList();
    }

    // Найти категорию по ID
    public Category getCategoryById(Long id) {
        return entityManager.find(Category.class, id);
    }

    // Создать новую категорию
    @Transactional
    public Category createCategory(Category category) {
        // Проверяем, нет ли уже категории с таким именем
        Long count = entityManager.createQuery(
                        "SELECT COUNT(c) FROM Category c WHERE c.name = :name", Long.class)
                .setParameter("name", category.getName())
                .getSingleResult();

        if (count > 0) {
            throw new RuntimeException("Категория с именем '" + category.getName() + "' уже существует");
        }

        entityManager.persist(category);
        return category;
    }

    // Обновить категорию
    @Transactional
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = entityManager.find(Category.class, id);
        if (category == null) {
            throw new RuntimeException("Категория с ID " + id + " не найдена");
        }

        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());

        return entityManager.merge(category);
    }

    // Удалить категорию
    @Transactional
    public void deleteCategory(Long id) {
        Category category = entityManager.find(Category.class, id);
        if (category == null) {
            throw new RuntimeException("Категория с ID " + id + " не найдена");
        }

        // Проверяем, есть ли продукты в этой категории
        Long productCount = entityManager.createQuery(
                        "SELECT COUNT(p) FROM Product p WHERE p.category.categoryId = :categoryId", Long.class)
                .setParameter("categoryId", id)
                .getSingleResult();

        if (productCount > 0) {
            throw new RuntimeException("Нельзя удалить категорию, в которой есть продукты");
        }

        entityManager.remove(category);
    }
}