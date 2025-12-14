<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Авторы</title>
</head>
<body>
<h1>Авторы</h1>

<h2>Добавить автора</h2>
<form action="/authors/save" method="post">
    ФИО: <input type="text" name="fio" required><br>
    Псевдоним: <input type="text" name="nickname"><br>
    Дата рождения: <input type="text" name="birthDate"><br>
    Описание: <textarea name="description"></textarea><br>
    <button type="submit">Сохранить</button>
</form>

<hr>

<h2>Список авторов</h2>
<table border="1">
    <tr>
        <th>ID</th>
        <th>ФИО</th>
        <th>Псевдоним</th>
        <th>Дата рождения</th>
        <th>Действия</th>
    </tr>
    <c:forEach items="${authors}" var="author">
        <tr>
            <td>${author.id}</td>
            <td>${author.fio}</td>
            <td>${author.nickname}</td>
            <td>${author.birthDate}</td>
            <td>
                <a href="/authors/delete/${author.id}"
                   onclick="return confirm('Удалить?')">Удалить</a>
            </td>
        </tr>
    </c:forEach>
</table>

<br>
<a href="/">На главную</a>
</body>
</html>