<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Авторы</title>
    <script>
        function setEditForm(authorId, fio, nickname, birthDate, description) {
            document.getElementById('editId').value = authorId;
            document.getElementById('editFio').value = fio;
            document.getElementById('editNickname').value = nickname || '';
            document.getElementById('editBirthDate').value = birthDate || '';
            document.getElementById('editDescription').value = description || '';

            // Прокручиваем к форме редактирования
            document.getElementById('editForm').scrollIntoView();
        }
    </script>
</head>
<body>
<h1>Авторы</h1>

<h2>${editAuthor.id != null ? 'Редактировать автора' : 'Добавить автора'}</h2>
<form action="/authors/save" method="post">
    <!-- Скрытое поле для ID при редактировании -->
    <input type="hidden" name="id" value="${editAuthor.id}">

    ФИО: <input type="text" name="fio" value="${editAuthor.fio}" required><br>
    Псевдоним: <input type="text" name="nickname" value="${editAuthor.nickname}"><br>
    Дата рождения: <input type="text" name="birthDate" value="${editAuthor.birthDate}"><br>
    Описание: <textarea name="description">${editAuthor.description}</textarea><br>
    <button type="submit">${editAuthor.id != null ? 'Обновить' : 'Сохранить'}</button>
    <c:if test="${editAuthor.id != null}">
        <a href="/authors">Отмена</a>
    </c:if>
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
                <a href="/authors/edit/${author.id}">Редактировать</a> |
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