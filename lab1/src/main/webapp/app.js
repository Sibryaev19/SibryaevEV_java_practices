const API_BASE = 'http://localhost:8081/lab1/api';

class ProductManager {
    constructor() {
        this.currentCategory = null;
        this.currentProduct = null;
        this.init();
    }

    async init() {
        this.setupEventListeners();
        await this.loadCategories();
        await this.loadProducts();
    }

    setupEventListeners() {
        // Форма категорий
        document.getElementById('categoryForm').addEventListener('submit', (e) => {
            e.preventDefault();
            this.saveCategory();
        });

        document.getElementById('cancelCategoryBtn').addEventListener('click', () => {
            this.resetCategoryForm();
        });

        // Форма продуктов
        document.getElementById('productForm').addEventListener('submit', (e) => {
            e.preventDefault();
            this.saveProduct();
        });

        document.getElementById('cancelProductBtn').addEventListener('click', () => {
            this.resetProductForm();
        });
    }

    // ===== КАТЕГОРИИ =====

    async loadCategories() {
        try {
            const response = await fetch(`${API_BASE}/categories`);
            const categories = await response.json();
            this.renderCategories(categories);
            this.updateCategorySelect(categories);
        } catch (error) {
            this.showMessage('categoryMessage', 'Ошибка загрузки категорий', 'error');
        }
    }

    renderCategories(categories) {
        const tbody = document.getElementById('categoriesList');
        tbody.innerHTML = '';

        categories.forEach(category => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${category.categoryId}</td>
                <td>${category.name}</td>
                <td>${category.description || 'нет описания'}</td>
                <td class="actions">
                    <button class="edit" onclick="app.editCategory(${category.categoryId}, '${category.name}', '${category.description || ''}')">✏️</button>
                    <button class="delete" onclick="app.deleteCategory(${category.categoryId})">🗑️</button>
                </td>
            `;
            tbody.appendChild(row);
        });
    }

    updateCategorySelect(categories) {
        const select = document.getElementById('productCategory');
        select.innerHTML = '<option value="">Выберите категорию</option>';

        categories.forEach(category => {
            const option = document.createElement('option');
            option.value = category.categoryId;
            option.textContent = category.name;
            select.appendChild(option);
        });
    }

    async saveCategory() {
        const categoryData = {
            name: document.getElementById('categoryName').value,
            description: document.getElementById('categoryDescription').value
        };

        const categoryId = document.getElementById('categoryId').value;

        try {
            let response;
            if (categoryId) {
                // Редактирование
                response = await fetch(`${API_BASE}/categories/${categoryId}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(categoryData)
                });
            } else {
                // Создание
                response = await fetch(`${API_BASE}/categories`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(categoryData)
                });
            }

            if (response.ok) {
                this.showMessage('categoryMessage',
                    categoryId ? 'Категория обновлена!' : 'Категория создана!',
                    'success');
                this.resetCategoryForm();
                await this.loadCategories();
            } else {
                const error = await response.text();
                this.showMessage('categoryMessage', error, 'error');
            }
        } catch (error) {
            this.showMessage('categoryMessage', 'Ошибка сохранения', 'error');
        }
    }

    editCategory(id, name, description) {
        document.getElementById('categoryId').value = id;
        document.getElementById('categoryName').value = name;
        document.getElementById('categoryDescription').value = description;
        document.getElementById('saveCategoryBtn').textContent = '💾 Обновить категорию';
    }

    async deleteCategory(id) {
        if (!confirm('Удалить эту категорию?')) return;

        try {
            const response = await fetch(`${API_BASE}/categories/${id}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                this.showMessage('categoryMessage', 'Категория удалена!', 'success');
                await this.loadCategories();
            } else {
                const error = await response.text();
                this.showMessage('categoryMessage', error, 'error');
            }
        } catch (error) {
            this.showMessage('categoryMessage', 'Ошибка удаления', 'error');
        }
    }

    resetCategoryForm() {
        document.getElementById('categoryForm').reset();
        document.getElementById('categoryId').value = '';
        document.getElementById('saveCategoryBtn').textContent = '💾 Сохранить категорию';
    }

    // ===== ПРОДУКТЫ =====

    async loadProducts() {
        try {
            const response = await fetch(`${API_BASE}/products`);
            const products = await response.json();
            this.renderProducts(products);
        } catch (error) {
            this.showMessage('productMessage', 'Ошибка загрузки продуктов', 'error');
        }
    }

    renderProducts(products) {
        const tbody = document.getElementById('productsList');
        tbody.innerHTML = '';

        products.forEach(product => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${product.productId}</td>
                <td>${product.name}</td>
                <td>${product.price} ₽</td>
                <td>${product.quantity} шт.</td>
                <td>${product.category ? product.category.name : 'Без категории'}</td>
                <td class="actions">
                    <button class="edit" onclick="app.editProduct(${product.productId}, '${product.name}', '${product.description || ''}', ${product.price}, ${product.quantity}, ${product.category ? product.category.categoryId : 'null'})">✏️</button>
                    <button class="delete" onclick="app.deleteProduct(${product.productId})">🗑️</button>
                </td>
            `;
            tbody.appendChild(row);
        });
    }

    async saveProduct() {
        const productData = {
            name: document.getElementById('productName').value,
            description: document.getElementById('productDescription').value,
            price: parseFloat(document.getElementById('productPrice').value),
            quantity: parseInt(document.getElementById('productQuantity').value),
            category: {
                categoryId: parseInt(document.getElementById('productCategory').value)
            }
        };

        const productId = document.getElementById('productId').value;

        try {
            let response;
            if (productId) {
                // Редактирование
                response = await fetch(`${API_BASE}/products/${productId}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(productData)
                });
            } else {
                // Создание
                response = await fetch(`${API_BASE}/products`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(productData)
                });
            }

            if (response.ok) {
                this.showMessage('productMessage',
                    productId ? 'Продукт обновлен!' : 'Продукт создан!',
                    'success');
                this.resetProductForm();
                await this.loadProducts();
            } else {
                const error = await response.text();
                this.showMessage('productMessage', error, 'error');
            }
        } catch (error) {
            this.showMessage('productMessage', 'Ошибка сохранения', 'error');
        }
    }

    editProduct(id, name, description, price, quantity, categoryId) {
        document.getElementById('productId').value = id;
        document.getElementById('productName').value = name;
        document.getElementById('productDescription').value = description;
        document.getElementById('productPrice').value = price;
        document.getElementById('productQuantity').value = quantity;
        document.getElementById('productCategory').value = categoryId || '';
        document.getElementById('saveProductBtn').textContent = '💾 Обновить продукт';
    }

    async deleteProduct(id) {
        if (!confirm('Удалить этот продукт?')) return;

        try {
            const response = await fetch(`${API_BASE}/products/${id}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                this.showMessage('productMessage', 'Продукт удален!', 'success');
                await this.loadProducts();
            } else {
                const error = await response.text();
                this.showMessage('productMessage', error, 'error');
            }
        } catch (error) {
            this.showMessage('productMessage', 'Ошибка удаления', 'error');
        }
    }

    resetProductForm() {
        document.getElementById('productForm').reset();
        document.getElementById('productId').value = '';
        document.getElementById('saveProductBtn').textContent = '💾 Сохранить продукт';
    }

    // ===== УТИЛИТЫ =====

    showMessage(elementId, text, type) {
        const element = document.getElementById(elementId);
        element.innerHTML = text;
        element.className = `message ${type}`;

        setTimeout(() => {
            element.innerHTML = '';
            element.className = 'message';
        }, 3000);
    }
}

// Инициализация приложения
const app = new ProductManager();