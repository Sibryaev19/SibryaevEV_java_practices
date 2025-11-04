-- ОСТАНАВЛИВАЕМСЯ ПРИ ОШИБКАХ
\set ON_ERROR_STOP on

-- Создаём БД, если её ещё нет (требует подключение к системной БД 'postgres')
SELECT 'CREATE DATABASE product_db' 
WHERE NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'product_db')\gexec

-- Подключаемся к базе данных
\connect product_db;

-- Таблица категорий
CREATE TABLE IF NOT EXISTS Category (
    category_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT DEFAULT 'нет описания'
);

-- Таблица продуктов
CREATE TABLE IF NOT EXISTS Product (
    product_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT DEFAULT 'нет описания',
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    quantity INTEGER NOT NULL CHECK (quantity >= 0) DEFAULT 0,
    category_id BIGINT REFERENCES Category(category_id) ON DELETE SET NULL
);

-- Добавляем тестовые данные в категории
INSERT INTO Category (name, description) VALUES
    ('Электроника', 'Электронные устройства и гаджеты')
ON CONFLICT (name) DO NOTHING;

INSERT INTO Category (name, description) VALUES
    ('Книги', 'Художественная и учебная литература')
ON CONFLICT (name) DO NOTHING;

INSERT INTO Category (name, description) VALUES
    ('Одежда', 'Одежда и аксессуары')
ON CONFLICT (name) DO NOTHING;

INSERT INTO Category (name, description) VALUES
    ('Продукты питания', 'Пищевые продукты')
ON CONFLICT (name) DO NOTHING;

-- Добавляем тестовые данные в продукты
INSERT INTO Product (name, description, price, quantity, category_id)
SELECT 'Смартфон Samsung', 'Современный смартфон с большим экраном', 29999.99, 15, c.category_id
FROM Category c WHERE c.name = 'Электроника'
AND NOT EXISTS (SELECT 1 FROM Product WHERE name = 'Смартфон Samsung');

INSERT INTO Product (name, description, price, quantity, category_id)
SELECT 'Ноутбук ASUS', 'Игровой ноутбук с мощной видеокартой', 75999.50, 8, c.category_id
FROM Category c WHERE c.name = 'Электроника'
AND NOT EXISTS (SELECT 1 FROM Product WHERE name = 'Ноутбук ASUS');

INSERT INTO Product (name, description, price, quantity, category_id)
SELECT 'Java для начинающих', 'Учебник по программированию на Java', 1500.00, 25, c.category_id
FROM Category c WHERE c.name = 'Книги'
AND NOT EXISTS (SELECT 1 FROM Product WHERE name = 'Java для начинающих');

INSERT INTO Product (name, description, price, quantity, category_id)
SELECT 'Футболка хлопковая', 'Комфортная футболка из 100% хлопка', 899.99, 50, c.category_id
FROM Category c WHERE c.name = 'Одежда'
AND NOT EXISTS (SELECT 1 FROM Product WHERE name = 'Футболка хлопковая');

INSERT INTO Product (name, description, price, quantity, category_id)
SELECT 'Джинсы классические', 'Синие джинсы прямого кроя', 2499.00, 30, c.category_id
FROM Category c WHERE c.name = 'Одежда'
AND NOT EXISTS (SELECT 1 FROM Product WHERE name = 'Джинсы классические');

INSERT INTO Product (name, description, price, quantity, category_id)
SELECT 'Шоколад молочный', 'Швейцарский молочный шоколад', 199.50, 100, c.category_id
FROM Category c WHERE c.name = 'Продукты питания'
AND NOT EXISTS (SELECT 1 FROM Product WHERE name = 'Шоколад молочный');

INSERT INTO Product (name, description, price, quantity, category_id)
SELECT 'Наушники беспроводные', 'Bluetooth наушники с шумоподавлением', 4999.00, 20, c.category_id
FROM Category c WHERE c.name = 'Электроника'
AND NOT EXISTS (SELECT 1 FROM Product WHERE name = 'Наушники беспроводные');

INSERT INTO Product (name, description, price, quantity, category_id)
SELECT 'Война и мир', 'Роман Льва Толстого', 850.00, 12, c.category_id
FROM Category c WHERE c.name = 'Книги'
AND NOT EXISTS (SELECT 1 FROM Product WHERE name = 'Война и мир');