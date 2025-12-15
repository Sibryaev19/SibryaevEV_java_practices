// Базовый URL API
const API_URL = '/api';

// DOM элементы
const authorForm = document.getElementById('authorForm');
const bookForm = document.getElementById('bookForm');
const authorsList = document.getElementById('authorsList');
const booksList = document.getElementById('booksList');
const authorSelect = document.getElementById('bookAuthorId');
const cancelAuthorEdit = document.getElementById('cancelAuthorEdit');
const cancelBookEdit = document.getElementById('cancelBookEdit');

// Текущие редактируемые объекты
let editingAuthorId = null;
let editingBookId = null;

// ==================== АВТОРЫ ====================

// Загрузить всех авторов
async function loadAuthors() {
    try {
        const response = await fetch(`${API_URL}/authors`);
        if (!response.ok) throw new Error('Ошибка загрузки авторов');
        const authors = await response.json();

        renderAuthors(authors);
        updateAuthorSelect(authors); // Обновить выпадающий список для книг
    } catch (error) {
        authorsList.innerHTML = `<div class="empty error">Ошибка: ${error.message}</div>`;
    }
}

// Отобразить авторов в таблице
function renderAuthors(authors) {
    if (!authors || authors.length === 0) {
        authorsList.innerHTML = '<div class="empty">Нет авторов. Добавьте первого!</div>';
        return;
    }

    let html = `
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>ФИО</th>
                    <th>Дата рождения</th>
                    <th>Действия</th>
                </tr>
            </thead>
            <tbody>
    `;

    authors.forEach(author => {
        html += `
            <tr>
                <td>${author.id}</td>
                <td><strong>${author.fio || ''}</strong></td>
                <td>${author.birthDate || '-'}</td>
                <td class="actions">
                    <button class="btn btn-edit" onclick="editAuthor(${author.id})">
                        <i class="fas fa-edit"></i> Изменить
                    </button>
                    <button class="btn btn-danger" onclick="deleteAuthor(${author.id})">
                        <i class="fas fa-trash"></i> Удалить
                    </button>
                </td>
            </tr>
        `;
    });

    html += '</tbody></table>';
    authorsList.innerHTML = html;
}

// Обработка формы автора
authorForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const authorData = {
        fio: document.getElementById('fio').value,
        birthDate: document.getElementById('birthDate').value || null,
        description: document.getElementById('description').value || null
    };

    try {
        let url = `${API_URL}/authors`;
        let method = 'POST';

        if (editingAuthorId) {
            url += `/${editingAuthorId}`;
            method = 'PUT';
            authorData.id = editingAuthorId;
        }

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(authorData)
        });

        if (!response.ok) throw new Error('Ошибка сохранения');

        resetAuthorForm();
        loadAuthors();
        loadBooks(); // Перезагрузить книги, т.к. мог измениться автор
    } catch (error) {
        alert(`Ошибка: ${error.message}`);
    }
});

// Редактировать автора
async function editAuthor(id) {
    try {
        const response = await fetch(`${API_URL}/authors/${id}`);
        if (!response.ok) throw new Error('Автор не найден');

        const author = await response.json();

        // Заполняем форму
        document.getElementById('authorId').value = author.id;
        document.getElementById('fio').value = author.fio || '';
        document.getElementById('birthDate').value = author.birthDate || '';
        document.getElementById('description').value = author.description || '';

        // Меняем заголовок
        document.getElementById('authorFormTitle').textContent = 'Редактировать автора';

        // Показываем кнопку отмены
        editingAuthorId = id;
        cancelAuthorEdit.style.display = 'inline-block';

        // Прокрутка к форме
        document.querySelector('#authorForm').scrollIntoView({ behavior: 'smooth' });
    } catch (error) {
        alert(`Ошибка: ${error.message}`);
    }
}

// Удалить автора
async function deleteAuthor(id) {
    if (!confirm('Удалить автора? Все его книги также будут удалены!')) {
        return;
    }

    try {
        const response = await fetch(`${API_URL}/authors/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Ошибка удаления');

        loadAuthors();
        loadBooks(); // Книги автора удалятся каскадно
    } catch (error) {
        alert(`Ошибка: ${error.message}`);
    }
}

// Сброс формы автора
function resetAuthorForm() {
    authorForm.reset();
    document.getElementById('authorId').value = '';
    document.getElementById('authorFormTitle').textContent = 'Добавить нового автора';
    editingAuthorId = null;
    cancelAuthorEdit.style.display = 'none';
}

// ==================== КНИГИ ====================

// Загрузить все книги
async function loadBooks() {
    try {
        const response = await fetch(`${API_URL}/books`);
        if (!response.ok) throw new Error('Ошибка загрузки книг');
        const books = await response.json();

        renderBooks(books);
    } catch (error) {
        booksList.innerHTML = `<div class="empty error">Ошибка: ${error.message}</div>`;
    }
}

// Отобразить книги в таблице
function renderBooks(books) {
    if (!books || books.length === 0) {
        booksList.innerHTML = '<div class="empty">Нет книг. Добавьте первую!</div>';
        return;
    }

    let html = `
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Название</th>
                    <th>Автор</th>
                    <th>Год</th>
                    <th>Действия</th>
                </tr>
            </thead>
            <tbody>
    `;

    books.forEach(book => {
        html += `
            <tr>
                <td>${book.id}</td>
                <td><strong>${book.name || ''}</strong></td>
                <td>${book.author?.fio || 'Автор не найден'}</td>
                <td>${book.publicationYear || '-'}</td>
                <td class="actions">
                    <button class="btn btn-edit" onclick="editBook(${book.id})">
                        <i class="fas fa-edit"></i> Изменить
                    </button>
                    <button class="btn btn-danger" onclick="deleteBook(${book.id})">
                        <i class="fas fa-trash"></i> Удалить
                    </button>
                </td>
            </tr>
        `;
    });

    html += '</tbody></table>';
    booksList.innerHTML = html;
}

// Обновить выпадающий список авторов
function updateAuthorSelect(authors) {
    authorSelect.innerHTML = '<option value="">Выберите автора...</option>';

    authors.forEach(author => {
        const option = document.createElement('option');
        option.value = author.id;
        option.textContent = `${author.fio} (ID: ${author.id})`;
        authorSelect.appendChild(option);
    });
}

// Обработка формы книги
bookForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const bookData = {
        name: document.getElementById('bookName').value,
        author: { id: parseInt(document.getElementById('bookAuthorId').value) },
        publicationYear: document.getElementById('publicationYear').value || null,
        pageNum: document.getElementById('pageNum').value ? parseInt(document.getElementById('pageNum').value) : null,
        description: document.getElementById('bookDescription').value || null
    };

    try {
        let url = `${API_URL}/books`;
        let method = 'POST';

        if (editingBookId) {
            url += `/${editingBookId}`;
            method = 'PUT';
            bookData.id = editingBookId;
        }

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(bookData)
        });

        if (!response.ok) throw new Error('Ошибка сохранения');

        resetBookForm();
        loadBooks();
    } catch (error) {
        alert(`Ошибка: ${error.message}`);
    }
});

// Редактировать книгу
async function editBook(id) {
    try {
        const response = await fetch(`${API_URL}/books/${id}`);
        if (!response.ok) throw new Error('Книга не найдена');

        const book = await response.json();

        // Заполняем форму
        document.getElementById('bookId').value = book.id;
        document.getElementById('bookName').value = book.name || '';
        document.getElementById('bookAuthorId').value = book.author?.id || '';
        document.getElementById('publicationYear').value = book.publicationYear || '';
        document.getElementById('pageNum').value = book.pageNum || '';
        document.getElementById('bookDescription').value = book.description || '';

        // Меняем заголовок
        document.getElementById('bookFormTitle').textContent = 'Редактировать книгу';

        // Показываем кнопку отмены
        editingBookId = id;
        cancelBookEdit.style.display = 'inline-block';

        // Прокрутка к форме
        document.querySelector('#bookForm').scrollIntoView({ behavior: 'smooth' });
    } catch (error) {
        alert(`Ошибка: ${error.message}`);
    }
}

// Удалить книгу
async function deleteBook(id) {
    if (!confirm('Удалить книгу?')) {
        return;
    }

    try {
        const response = await fetch(`${API_URL}/books/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Ошибка удаления');

        loadBooks();
    } catch (error) {
        alert(`Ошибка: ${error.message}`);
    }
}

// Сброс формы книги
function resetBookForm() {
    bookForm.reset();
    document.getElementById('bookId').value = '';
    document.getElementById('bookFormTitle').textContent = 'Добавить новую книгу';
    editingBookId = null;
    cancelBookEdit.style.display = 'none';
}

// ==================== ИНИЦИАЛИЗАЦИЯ ====================

// Обработчики кнопок отмены
cancelAuthorEdit.addEventListener('click', resetAuthorForm);
cancelBookEdit.addEventListener('click', resetBookForm);

// Загрузка данных при старте
document.addEventListener('DOMContentLoaded', () => {
    loadAuthors();
    loadBooks();

    // Установить текущую дату по умолчанию для поля даты рождения
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('birthDate').max = today;
});

// Обработка ошибок fetch
window.addEventListener('unhandledrejection', event => {
    console.error('Unhandled promise rejection:', event.reason);
});