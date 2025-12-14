<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Книги</title>
</head>
<body>
<h1>Книги</h1>

<h2>${editBook.id != null ? 'Редактировать книгу' : 'Добавить книгу'}</h2>
<form action="/books/save" method="post">
    <!-- Скрытое поле для ID при редактировании -->
    <input type="hidden" name="id" value="${editBook.id}">

    Название: <input type="text" name="name" value="${editBook.name}" required><br>
    Автор:
    <select name="author.id" required>
        <option value="">Выберите автора</option>
        <c:forEach items="${authors}" var="author">
            <option value="${author.id}"
                ${editBook.author != null && editBook.author.id == author.id ? 'selected' : ''}>
                    ${author.fio}
            </option>
        </c:forEach>
    </select><br>
    Год издания: <input type="text" name="publicationYear" value="${editBook.publicationYear}"><br>
    Страниц: <input type="number" name="pageNum" value="${editBook.pageNum}"><br>
    Рейтинг: <input type="number" step="0.1" name="rating" value="${editBook.rating}"><br>
    Описание: <textarea name="description">${editBook.description}</textarea><br>
    <button type="submit">${editBook.id != null ? 'Обновить' : 'Сохранить'}</button>
    <c:if test="${editBook.id != null}">
        <a href="/books">Отмена</a>
    </c:if>
</form>

<hr>

<h2>Список книг</h2>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Название</th>
        <th>Автор</th>
        <th>Год</th>
        <th>Страниц</th>
        <th>Рейтинг</th>
        <th>Действия</th>
    </tr>
    <c:forEach items="${books}" var="book">
        <tr>
            <td>${book.id}</td>
            <td>${book.name}</td>
            <td>${book.author.fio}</td>
            <td>${book.publicationYear}</td>
            <td>${book.pageNum}</td>
            <td>${book.rating}</td>
            <td>
                <a href="/books/edit/${book.id}">Редактировать</a> |
                <a href="/books/delete/${book.id}"
                   onclick="return confirm('Удалить?')">Удалить</a>
            </td>
        </tr>
    </c:forEach>
</table>

<br>
<a href="/">На главную</a>
</body>
</html>