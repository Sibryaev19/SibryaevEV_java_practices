<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Книги</title>
</head>
<body>
<h1>Книги</h1>

<h2>Добавить книгу</h2>
<form action="/books/save" method="post">
    Название: <input type="text" name="name" required><br>
    Автор:
    <select name="author.id" required>
        <option value="">Выберите автора</option>
        <c:forEach items="${authors}" var="author">
            <option value="${author.id}">${author.fio}</option>
        </c:forEach>
    </select><br>
    Год издания: <input type="text" name="publicationYear"><br>
    Страниц: <input type="number" name="pageNum"><br>
    Рейтинг: <input type="number" step="0.1" name="rating"><br>
    Описание: <textarea name="description"></textarea><br>
    <button type="submit">Сохранить</button>
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