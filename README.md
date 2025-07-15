# Smartix Test Task

Spring Boot приложение, которое получает данные о товарах из внешнего сервиса [Fake Store API](https://fakestoreapi.com), сохраняет их в базу данных и предоставляет REST API для работы с этими данными.

## Технологический стек

- Java 21
- Spring Boot 3.2.0
- PostgreSQL 17
- Spring Data JPA
- Lombok
- Swagger UI (OpenAPI 3.0)

## Запуск приложения

### Предварительные требования

- JDK 21
- PostgreSQL 17
- Maven

### Настройка базы данных

```sql
CREATE DATABASE smartix;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE smartix TO postgres;
```

### Сборка и запуск

```bash
mvn clean install
java -jar target/smartix-test-0.0.1-SNAPSHOT.jar
```

## Документация API

### Основные эндпоинты

#### Импорт товаров из внешнего API

- **Метод**: POST
- **URL**: /api/products/import
- **Описание**: Импортирует данные о товарах из внешнего API (https://fakestoreapi.com/products) и сохраняет их в базу данных.
- **Ответ**: Список импортированных товаров

#### Создание товара

- **Метод**: POST
- **URL**: /api/products
- **Тело запроса**:
```json
{
  "title": "Название товара",
  "price": 19.99,
  "description": "Описание товара",
  "category": "Категория",
  "image": "URL изображения",
  "rating": {
    "rate": 4.5,
    "count": 120
  }
}
```
- **Ответ**: Созданный товар с присвоенным ID

#### Получение товара по ID

- **Метод**: GET
- **URL**: /api/products/{id}
- **Параметры пути**: id - ID товара
- **Ответ**: Товар с указанным ID

#### Получение списка всех товаров

- **Метод**: GET
- **URL**: /api/products
- **Параметры запроса**:
  - page - номер страницы (по умолчанию 0)
  - size - размер страницы (по умолчанию 10)
- **Ответ**: Страница товаров с метаданными пагинации

#### Обновление товара

- **Метод**: PUT
- **URL**: /api/products/{id}
- **Параметры пути**: id - ID товара
- **Тело запроса**: JSON с обновленными полями товара
- **Ответ**: Обновленный товар

#### Удаление товара

- **Метод**: DELETE
- **URL**: /api/products/{id}
- **Параметры пути**: id - ID товара
- **Ответ**: 204 No Content при успешном удалении

#### Фильтрация товаров по цене

- **Метод**: GET
- **URL**: /api/products/filter
- **Параметры запроса**:
  - minPrice - минимальная цена
  - maxPrice - максимальная цена
  - page - номер страницы (по умолчанию 0)
  - size - размер страницы (по умолчанию 10)
- **Ответ**: Страница товаров, соответствующих заданному диапазону цен, с метаданными пагинации

#### Получение списка уникальных категорий

- **Метод**: GET
- **URL**: /api/products/categories
- **Ответ**: Список уникальных категорий товаров

## Структура базы данных

### Таблица products

- id (Long, PK)
- title (String)
- price (BigDecimal)
- description (String)
- category_id (Long, FK)
- image (String)
- rating_id (Long, FK)

### Таблица categories

- id (Long, PK)
- name (String, unique)

### Таблица ratings

- id (Long, PK)
- rate (Double)
- count (Integer)
