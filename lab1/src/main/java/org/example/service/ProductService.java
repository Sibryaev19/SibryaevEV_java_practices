package org.example.service;

import org.example.entity.Product;
import org.example.entity.Category;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import java.math.BigDecimal;
import java.util.List;

@Stateless
public class ProductService {

    @PersistenceContext
    private EntityManager entityManager;

    // Получить все продукты
    public List<Product> getAllProducts() {
        return entityManager.createQuery(
                        "SELECT p FROM Product p LEFT JOIN FETCH p.category", Product.class)
                .getResultList();
    }

    // Найти продукт по ID
    public Product getProductById(Long id) {
        return entityManager.find(Product.class, id);
    }

    // Создать новый продукт
    @jakarta.ejb.TransactionAttribute(jakarta.ejb.TransactionAttributeType.REQUIRED)
    public Product createProduct(Product product) {
        // Проверяем категорию
        if (product.getCategory() != null && product.getCategory().getCategoryId() != null) {
            Category category = entityManager.find(Category.class, product.getCategory().getCategoryId());
            if (category == null) {
                throw new RuntimeException("Категория с ID " + product.getCategory().getCategoryId() + " не найдена");
            }
            product.setCategory(category);
        }

        // Бизнес-правила
        validateProduct(product);

        entityManager.persist(product);
        return product;
    }

    // Обновить продукт
    @jakarta.ejb.TransactionAttribute(jakarta.ejb.TransactionAttributeType.REQUIRED)
    public Product updateProduct(Long id, Product productDetails) {
        Product product = entityManager.find(Product.class, id);
        if (product == null) {
            throw new RuntimeException("Продукт с ID " + id + " не найден");
        }

        // Обновляем категорию если нужно
        if (productDetails.getCategory() != null && productDetails.getCategory().getCategoryId() != null) {
            Category category = entityManager.find(Category.class, productDetails.getCategory().getCategoryId());
            product.setCategory(category);
        }

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());

        validateProduct(product);

        return entityManager.merge(product);
    }

    // Удалить продукт
    @jakarta.ejb.TransactionAttribute(jakarta.ejb.TransactionAttributeType.REQUIRED)
    public void deleteProduct(Long id) {
        Product product = entityManager.find(Product.class, id);
        if (product == null) {
            throw new RuntimeException("Продукт с ID " + id + " не найден");
        }
        entityManager.remove(product);
    }

    // Поиск продуктов по категории
    public List<Product> getProductsByCategory(Long categoryId) {
        return entityManager.createQuery(
                        "SELECT p FROM Product p WHERE p.category.categoryId = :categoryId", Product.class)
                .setParameter("categoryId", categoryId)
                .getResultList();
    }

    // Бизнес-валидация
    private void validateProduct(Product product) {
        if (product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Цена не может быть отрицательной");
        }
        if (product.getQuantity() < 0) {
            throw new RuntimeException("Количество не может быть отрицательным");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new RuntimeException("Название продукта обязательно");
        }
    }
}