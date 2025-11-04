-- Инициализация схемы и данных (идемпотентно для PostgreSQL)

-- Таблица категорий
CREATE TABLE IF NOT EXISTS category (
    category_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT DEFAULT 'нет описания'
);

-- Таблица продуктов
CREATE TABLE IF NOT EXISTS product (
    product_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT DEFAULT 'нет описания',
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    quantity INTEGER NOT NULL CHECK (quantity >= 0) DEFAULT 0,
    category_id BIGINT REFERENCES category(category_id) ON DELETE SET NULL
);

-- Данные категорий (вставка, если нет записи с таким именем)
INSERT INTO category(name, description)
SELECT 'Электроника', 'Электронные устройства и гаджеты'
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Электроника');

INSERT INTO category(name, description)
SELECT 'Книги', 'Художественная и учебная литература'
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Книги');

INSERT INTO category(name, description)
SELECT 'Одежда', 'Одежда и аксессуары'
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Одежда');

INSERT INTO category(name, description)
SELECT 'Продукты питания', 'Пищевые продукты'
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Продукты питания');

-- Данные продуктов (вставка, если нет записи с таким именем)
INSERT INTO product(name, description, price, quantity, category_id)
SELECT 'Смартфон Samsung', 'Современный смартфон с большим экраном', 29999.99, 15, c.category_id
FROM category c WHERE c.name = 'Электроника'
AND NOT EXISTS (SELECT 1 FROM product WHERE name = 'Смартфон Samsung');

INSERT INTO product(name, description, price, quantity, category_id)
SELECT 'Ноутбук ASUS', 'Игровой ноутбук с мощной видеокартой', 75999.50, 8, c.category_id
FROM category c WHERE c.name = 'Электроника'
AND NOT EXISTS (SELECT 1 FROM product WHERE name = 'Ноутбук ASUS');

INSERT INTO product(name, description, price, quantity, category_id)
SELECT 'Java для начинающих', 'Учебник по программированию на Java', 1500.00, 25, c.category_id
FROM category c WHERE c.name = 'Книги'
AND NOT EXISTS (SELECT 1 FROM product WHERE name = 'Java для начинающих');

INSERT INTO product(name, description, price, quantity, category_id)
SELECT 'Футболка хлопковая', 'Комфортная футболка из 100% хлопка', 899.99, 50, c.category_id
FROM category c WHERE c.name = 'Одежда'
AND NOT EXISTS (SELECT 1 FROM product WHERE name = 'Футболка хлопковая');

INSERT INTO product(name, description, price, quantity, category_id)
SELECT 'Джинсы классические', 'Синие джинсы прямого кроя', 2499.00, 30, c.category_id
FROM category c WHERE c.name = 'Одежда'
AND NOT EXISTS (SELECT 1 FROM product WHERE name = 'Джинсы классические');

INSERT INTO product(name, description, price, quantity, category_id)
SELECT 'Шоколад молочный', 'Швейцарский молочный шоколад', 199.50, 100, c.category_id
FROM category c WHERE c.name = 'Продукты питания'
AND NOT EXISTS (SELECT 1 FROM product WHERE name = 'Шоколад молочный');

INSERT INTO product(name, description, price, quantity, category_id)
SELECT 'Наушники беспроводные', 'Bluetooth наушники с шумоподавлением', 4999.00, 20, c.category_id
FROM category c WHERE c.name = 'Электроника'
AND NOT EXISTS (SELECT 1 FROM product WHERE name = 'Наушники беспроводные');

INSERT INTO product(name, description, price, quantity, category_id)
SELECT 'Война и мир', 'Роман Льва Толстого', 850.00, 12, c.category_id
FROM category c WHERE c.name = 'Книги'
AND NOT EXISTS (SELECT 1 FROM product WHERE name = 'Война и мир');


