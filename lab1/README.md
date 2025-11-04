Лабораторная 1 — JakartaEE (WildFly + PostgreSQL)

Простое CRUD‑приложение (Категории/Продукты) на JakartaEE с REST API и фронтендом на чистом HTML/JS. Репозиторий содержит скрипты для автоматической подготовки БД и WildFly.

## Что будет установлено
- Java 11 (JDK)
- Maven 3.8+
- PostgreSQL (локально)
- WildFly 26+ (или совместимый)

## Быстрый старт (Windows, новичок)
1) Установите:
   - Java 11 (проверьте `java -version`)
   - Maven (`mvn -v`)
   - PostgreSQL (запомните пароль суперпользователя)
   - WildFly и задайте переменную окружения `WILDFLY_HOME` на папку WildFly

2) Запустите WildFly:
   - Откройте PowerShell, выполните:
     - `$env:WILDFLY_HOME\bin\add-user.bat` и создайте админа (`admin`/`admin`)
     - `$env:WILDFLY_HOME\bin\standalone.bat`
   - Консоль администратора: `http://localhost:9990/`

3) Подготовьте базу данных (автоматически):
   - Откройте PowerShell в корне проекта и выполните:
     - `psql -U postgres -h localhost -p 5432 -f .\sql_scripts\shop.sql`
     - при запросе пароля введите: `999111`
   - Скрипт:
     - создаёт БД `product_db`, если её нет
     - создаёт таблицы (если их нет)
     - наполняет начальными данными, не дублируя их

4) Настройте WildFly (драйвер + DataSource) автоматически:
   - В PowerShell из корня проекта:
     - `powershell -ExecutionPolicy Bypass -File .\scripts\setup.ps1 -WildFlyHome $env:WILDFLY_HOME`
   - Скрипт скачает JDBC‑драйвер PostgreSQL, добавит модуль и создаст DataSource `ProductDS` (`java:jboss/datasources/ProductDS`).

5) Соберите и задеплойте приложение:
   - `mvn clean package`
   - `mvn wildfly:deploy`

6) Откройте UI:
   - `http://localhost:8081/lab1/` (если у вас WildFly слушает 8080, а не 8081 — поменяйте `API_BASE` в `src/main/webapp/app.js` на `http://localhost:8080/lab1/api` и пересоберите)

## Структура важных файлов
- `src/main/resources/META-INF/persistence.xml` — PU `product-persistence-unit`, DS `ProductDS`
- `src/main/resources/META-INF/import.sql` — идемпотентная инициализация схемы/данных при наличии рабочей БД (исполняется Hibernate)
- `sql_scripts/shop.sql` — идемпотентный psql‑скрипт: создаёт БД и заполняет (для первого запуска)
- `scripts/setup.ps1` — автоматическая настройка WildFly (драйвер+DataSource)
- `scripts/wildfly-setup.cli` — CLI‑скрипт, если хотите запускать ручным способом

## API
- Категории:
  - GET `/api/categories`
  - GET `/api/categories/{id}`
  - POST `/api/categories`
  - PUT `/api/categories/{id}`
  - DELETE `/api/categories/{id}`
- Продукты:
  - GET `/api/products`
  - GET `/api/products/{id}`
  - GET `/api/products/category/{categoryId}`
  - POST `/api/products`
  - PUT `/api/products/{id}`
  - DELETE `/api/products/{id}`

## Частые проблемы и решения
- Не открывается UI по 8081
  - По умолчанию WildFly — 8080. Либо откройте `http://localhost:8080/lab1/`, либо поменяйте `API_BASE` в `src/main/webapp/app.js`.
- Ошибка подключения к БД / DataSource не работает
  - Убедитесь, что выполнили пункт 3 (shop.sql) и 4 (setup.ps1).
  - Проверьте в админ‑консоли WildFly: Runtime → Subsystems → Datasources → Test Connection.
- `import.sql` не выполнился
  - Он работает только если DataSource `ProductDS` успешно подключился к существующей БД. Для первого запуска используйте `sql_scripts/shop.sql`.
- Деплой не проходит через Maven
  - Проверьте, что WildFly запущен и доступен по `localhost:9990`, логин/пароль совпадают (`admin/admin`).

## Как это работает
- Первый запуск: `sql_scripts/shop.sql` через `psql` создаёт БД `product_db`, затем таблицы и данные.
- Приложение использует WildFly DataSource `ProductDS` → JPA (Hibernate) → сущности `Category`/`Product`.
- При старте приложения Hibernate подхватывает `META-INF/import.sql` и дополнительно синхронизирует схему/данные, если нужно (идемпотентно).

## Полезные команды
- Выполнить psql‑скрипт: `psql -U postgres -h localhost -p 5432 -f .\sql_scripts\shop.sql`
- Запуск WildFly: `$env:WILDFLY_HOME\bin\standalone.bat`
- Деплой: `mvn wildfly:deploy`
- Ан‑деплой: `mvn wildfly:undeploy`

## Настройка под другой порт/хост
- Обновите `scripts/setup.ps1` параметрами `-PgHost`, `-PgPort` и `-WildFlyHome`.
- При смене контекста/порта HTTP измените `API_BASE` в `src/main/webapp/app.js`.

## Данные по умолчанию
- PostgreSQL: user `postgres`, pass `999111`, port `5432`
- WildFly Admin: login `admin`, pass `admin`, mgmt port `9990`


