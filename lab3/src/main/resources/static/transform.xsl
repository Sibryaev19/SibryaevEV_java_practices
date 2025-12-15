<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <html>
            <head>
                <title>Book Catalog</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 20px; line-height: 1.6; }
                    h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }
                    h2 { color: #34495e; margin-top: 30px; }
                    table { border-collapse: collapse; width: 100%; margin: 20px 0; box-shadow: 0 1px 3px rgba(0,0,0,0.2); }
                    th { background-color: #3498db; color: white; font-weight: bold; }
                    th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
                    tr:nth-child(even) { background-color: #f8f9fa; }
                    tr:hover { background-color: #e8f4fc; }
                    .nav { background-color: #2c3e50; padding: 15px; border-radius: 5px; margin-bottom: 20px; }
                    .nav a { color: white; margin-right: 15px; text-decoration: none; padding: 8px 15px; border-radius: 3px; }
                    .nav a:hover { background-color: #3498db; }
                    .actions a { display: inline-block; margin: 2px; padding: 4px 10px; background-color: #ecf0f1; border-radius: 3px; text-decoration: none; color: #2c3e50; }
                    .actions a:hover { background-color: #bdc3c7; }
                    .info-box { background-color: #e8f6f3; border-left: 4px solid #1abc9c; padding: 15px; margin: 20px 0; }
                </style>
            </head>
            <body>
                <!-- Навигационное меню -->
                <div class="nav">
                    <a href="/">🏠 Главная</a>
                    <a href="/api/authors">👥 Авторы (XML)</a>
                    <a href="/api/books">📚 Книги (XML)</a>
                    <a href="/api">ℹ️ API информация</a>
                    <a href="javascript:location.reload()">🔄 Обновить</a>
                </div>

                <xsl:choose>

                    <!-- API информация (HomeController) -->
                    <xsl:when test="HashMap">
                        <h1>📋 REST API Information</h1>
                        <div class="info-box">
                            <strong>Content Negotiation:</strong> Используйте заголовок Accept: application/json или application/xml
                        </div>

                        <h2>👥 Авторы</h2>
                        <ul>
                            <li><strong>GET всех авторов:</strong> <a href="/api/authors">/api/authors</a></li>
                            <li><strong>GET автора по ID:</strong> /api/authors/{id}</li>
                            <li><strong>POST создать автора:</strong> /api/authors</li>
                            <li><strong>PUT обновить автора:</strong> /api/authors/{id}</li>
                            <li><strong>DELETE автора:</strong> /api/authors/{id}</li>
                        </ul>

                        <h2>📚 Книги</h2>
                        <ul>
                            <li><strong>GET всех книг:</strong> <a href="/api/books">/api/books</a></li>
                            <li><strong>GET книги по ID:</strong> /api/books/{id}</li>
                            <li><strong>GET книги по автору:</strong> /api/books/author/{authorId}</li>
                            <li><strong>POST создать книгу:</strong> /api/books</li>
                            <li><strong>PUT обновить книгу:</strong> /api/books/{id}</li>
                            <li><strong>DELETE книгу:</strong> /api/books/{id}</li>
                        </ul>
                    </xsl:when>

                    <!-- Список авторов -->
                    <xsl:when test="authors">
                        <h1>👥 Список авторов</h1>
                        <div class="info-box">
                            Найдено авторов: <xsl:value-of select="count(authors/author)"/>
                            | <a href="/api">← Назад к API</a>
                        </div>

                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>ФИО</th>
                                    <th>Псевдоним</th>
                                    <th>Дата рождения</th>
                                    <th>Действия</th>
                                </tr>
                            </thead>
                            <tbody>
                                <xsl:for-each select="authors/author">
                                    <tr>
                                        <td><xsl:value-of select="id"/></td>
                                        <td><strong><xsl:value-of select="fio"/></strong></td>
                                        <td><xsl:value-of select="nickname"/></td>
                                        <td><xsl:value-of select="birthDate"/></td>
                                        <td class="actions">
                                            <a href="/api/authors/{id}">Просмотр</a>
                                            <a href="/api/books/author/{id}">Книги</a>
                                            <a href="/api/authors/{id}?format=json">JSON</a>
                                        </td>
                                    </tr>
                                </xsl:for-each>
                            </tbody>
                        </table>
                    </xsl:when>

                    <!-- Один автор -->
                    <xsl:when test="author">
                        <h1>👤 Автор: <xsl:value-of select="author/fio"/></h1>
                        <div class="info-box">
                            <strong>ID:</strong> <xsl:value-of select="author/id"/>
                            | <a href="/api/authors">← Все авторы</a>
                            | <a href="/api/books/author/{author/id}">📚 Книги этого автора</a>
                        </div>

                        <table>
                            <tr><th>Поле</th><th>Значение</th></tr>
                            <tr><td>ФИО</td><td><xsl:value-of select="author/fio"/></td></tr>
                            <tr><td>Псевдоним</td><td><xsl:value-of select="author/nickname"/></td></tr>
                            <tr><td>Дата рождения</td><td><xsl:value-of select="author/birthDate"/></td></tr>
                            <tr><td>Описание</td><td><xsl:value-of select="author/description"/></td></tr>
                        </table>

                        <xsl:if test="author/books">
                            <h2>📚 Книги этого автора</h2>
                            <table>
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Название</th>
                                        <th>Год</th>
                                        <th>Страниц</th>
                                        <th>Действия</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <xsl:for-each select="author/books/item">
                                        <tr>
                                            <td><xsl:value-of select="id"/></td>
                                            <td><strong><xsl:value-of select="name"/></strong></td>
                                            <td><xsl:value-of select="publicationYear"/></td>
                                            <td><xsl:value-of select="pageNum"/></td>
                                            <td class="actions">
                                                <a href="/api/books/{id}">Просмотр</a>
                                                <a href="/api/books/{id}?format=json">JSON</a>
                                            </td>
                                        </tr>
                                    </xsl:for-each>
                                </tbody>
                            </table>
                        </xsl:if>
                    </xsl:when>

                    <!-- Список книг -->
                    <xsl:when test="books">
                        <h1>📚 Список книг</h1>
                        <div class="info-box">
                            Найдено книг: <xsl:value-of select="count(books/book)"/>
                            | <a href="/api">← Назад к API</a>
                        </div>

                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Название</th>
                                    <th>Автор</th>
                                    <th>Год</th>
                                    <th>Страниц</th>
                                    <th>Действия</th>
                                </tr>
                            </thead>
                            <tbody>
                                <xsl:for-each select="books/book">
                                    <tr>
                                        <td><xsl:value-of select="id"/></td>
                                        <td><strong><xsl:value-of select="name"/></strong></td>
                                        <td>
                                            <xsl:choose>
                                                <xsl:when test="author/fio">
                                                    <a href="/api/authors/{author/id}">
                                                        <xsl:value-of select="author/fio"/>
                                                    </a>
                                                </xsl:when>
                                                <xsl:otherwise>Автор не указан</xsl:otherwise>
                                            </xsl:choose>
                                        </td>
                                        <td><xsl:value-of select="publicationYear"/></td>
                                        <td><xsl:value-of select="pageNum"/></td>
                                        <td class="actions">
                                            <a href="/api/books/{id}">Просмотр</a>
                                            <a href="/api/books/{id}?format=json">JSON</a>
                                            <a href="/api/authors/{author/id}">Автор</a>
                                        </td>
                                    </tr>
                                </xsl:for-each>
                            </tbody>
                        </table>
                    </xsl:when>

                    <!-- Одна книга -->
                    <xsl:when test="book">
                        <h1>📖 Книга: <xsl:value-of select="book/name"/></h1>
                        <div class="info-box">
                            <strong>ID:</strong> <xsl:value-of select="book/id"/>
                            | <a href="/api/books">← Все книги</a>
                            <xsl:if test="book/author/id">
                                | <a href="/api/authors/{book/author/id}">👤 К автору</a>
                            </xsl:if>
                        </div>

                        <table>
                            <tr><th>Поле</th><th>Значение</th></tr>
                            <tr><td>Название</td><td><xsl:value-of select="book/name"/></td></tr>
                            <tr><td>Автор</td>
                                <td>
                                    <xsl:choose>
                                        <xsl:when test="book/author/fio">
                                            <a href="/api/authors/{book/author/id}">
                                                <xsl:value-of select="book/author/fio"/>
                                            </a>
                                        </xsl:when>
                                        <xsl:otherwise>Автор не указан</xsl:otherwise>
                                    </xsl:choose>
                                </td>
                            </tr>
                            <tr><td>Год издания</td><td><xsl:value-of select="book/publicationYear"/></td></tr>
                            <tr><td>Количество страниц</td><td><xsl:value-of select="book/pageNum"/></td></tr>
                            <tr><td>Описание</td><td><xsl:value-of select="book/description"/></td></tr>
                        </table>
                    </xsl:when>

                    <!-- По умолчанию (отладка) -->
                    <xsl:otherwise>
                        <h1>XML Data</h1>
                        <div class="info-box">
                            Неизвестная структура XML. Отладка:
                        </div>
                        <pre><xsl:copy-of select="."/></pre>
                    </xsl:otherwise>

                </xsl:choose>

                <hr/>
                <div style="text-align: center; color: #7f8c8d; margin-top: 40px;">
                    <p>✅ XSL Transformation успешно применена</p>
                    <p>
                        <a href="/">🌐 Перейти к веб-интерфейсу</a> |
                        <a href="https://github.com">📖 Документация</a>
                    </p>
                </div>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>