# Проект "Управление пользователями"

Этот проект представляет собой простую веб-приложение для управления пользователями. Он включает в себя RESTful API для добавления, получения и удаления пользователей.

## Описание

### Технологии

Проект разработан с использованием следующих технологий:

- Java 17
- Spring Framework (Spring Boot) 3.1.5
- Spring Data JPA
- RESTful API
- Maven

### Компоненты

Проект состоит из следующих компонентов:

- `UserController`: Контроллер для обработки HTTP-запросов, связанных с пользователями.
- `UserService`: Интерфейс для управления пользователями.
- `UserServiceImpl`: Реализация интерфейса UserService, включая методы для сохранения, получения и удаления пользователей.
- `User`: Модель пользователя, представляющая собой сущность в базе данных.

## Установка и запуск

1. Клонируйте репозиторий:

   ```bash
   git clone https://github.com/_проект.git
   ```

2. Перейдите в каталог проекта:

   ```bash
   cd _проект
   ```

3. Соберите проект с использованием Maven:

   ```bash
   mvn clean install
   ```

4. Запустите приложение:

   ```bash
   java -jar target/_проект.jar
   ```

Приложение будет доступно по адресу `http://localhost:8080`.

## Использование API

### Добавление пользователя

Чтобы добавить нового пользователя, выполните POST-запрос на `/add` с параметрами `name` и `password`.

Пример:

```http
POST http://localhost:8080/add?name=John&password=secret
```

### Получение пользователя

Чтобы получить информацию о пользователе по его идентификатору, выполните GET-запрос на `/get` с параметром `id`.

Пример:

```http
GET http://localhost:8080/get?id=1
```

### Удаление пользователя

Чтобы удалить пользователя по его идентификатору, выполните DELETE-запрос на `/delete` с параметром `id`.

Пример:

```http
DELETE http://localhost:8080/delete?id=1
```

