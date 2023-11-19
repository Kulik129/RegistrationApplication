# RESTful API для управления пользователями

Этот проект представляет собой простой микросервис для управления пользователями. Он включает в себя RESTful API для
добавления, получения и удаления пользователей.

## Описание

### Технологии

Проект разработан с использованием следующих технологий:

- Java 17
- Spring Framework (Spring Boot) 3.1.5
- Spring Data JPA
- Spring Security
- RESTful API
- MySQL
- JUnit 5
- Maven

### Компоненты

Проект состоит из следующих компонентов:

- `CorsConfig`: Класс конфигурации для обработки междоменных запросов (CORS).
- `UserController`: Контроллер для обработки HTTP-запросов, связанных с пользователями.
- `GlobalExceptionHandler`: Глобальный обработчик исключений.
- `ApiValidationError`: Представляет детали ошибки валидации для ответов API.
- `UserDTO`: Поля используемые для передачи данных между слоями приложения.
- `ApiException`: Пользовательские исключения.
- `User`: Модель пользователя, представляющая собой сущность в базе данных.
- `UserRepository`: Интерфейс репозитория для сущностей User, расширяющий JpaRepository.
- `WebSecurityConfig`: Класс безопасности приложения, шифрование паролей и настройка доступа к ссылкам.
- `UserService`: Интерфейс для управления пользователями.
- `UserServiceImpl`: Реализация интерфейса UserService, включая методы для сохранения, получения и удаления
  пользователей.
- `UserValidator`: Класс валидатора для объектов User, проверяющий уникальность email и номера телефона.
- `application.properties`: Файл конфигурации для настройки приложения.

---

## Установка и запуск

**Примечание:** В `application.properties`, `allowed.origins=` поставьте хост на фактический адрес вашего приложения с
которого будут отправляться запросы к этому API, например: `allowed.origins=http://localhost:8090`.

1. Клонируйте репозиторий:

   ```bash
   git clone https://github.com/Kulik129/RegistrationApplication.git
   ```

2. Перейдите в каталог проекта:

   ```bash
   cd out/artifacts/registration_jar
   ```

3. Соберите проект с использованием Maven:

   ```bash
   mvn clean install
   ```

4. Запустите приложение:

   ```bash
   java -jar target/registration_jar
   ```
   Приложение будет доступно по адресу `http://localhost:8080`.

## Установка Java 17 на Linux:

1. Установите Java командой:

   ```bash
   sudo apt install openjdk-17-jre-headless
   ```
2. Установите Javac командой:

   ```bash
   apt install openjdk-17-jdk-headless
   ```

3. Обновите командой:

   ```bash
   sudo update
   ```

---

# _Ниже приведены примеры запросов и ожидаемых ответов для каждого эндпоинта._

## 1. Добавление нового пользователя

### Запрос:

```http
POST /api/v1/users/add
```

_**Content-Type: application/json**_

```json
{
  "first_name": "John",
  "last_name": "Doe",
  "date_of_birth": "1990-01-01",
  "phone_number": "89123456789",
  "email": "john.doe@example.com",
  "password": "securePassword"
}
```

### Ответ:

```http
HTTP/1.1 201 Created
Location: /add/123  # где 123 - идентификатор созданного пользователя
```

## 2. Обновление данных пользователя

### Запрос:

```http
PUT /api/v1/users/update-user-info/{id}
```

**_Content-Type: application/json_**

```json
{
  "first_name": "UpdatedFirstName",
  "last_name": "UpdatedLastName",
  "date_of_birth": "1990-01-02"
}
```

### Ответ:

```http
HTTP/1.1 200 OK
```

**_Content-Type: application/json_**

```json
{
  "id": 123,
  "first_name": "UpdatedFirstName",
  "last_name": "UpdatedLastName",
  "date_of_birth": "1990-01-02",
  "phone_number": "89123456789",
  "email": "john.doe@example.com",
  "registration_date": "2023-11-19T14:27:55",
  "subscription_end_date": "2044-11-23T14:31:11",
  "role": "USER",
  "active": true
}
```

## 3. Получение пользователя по идентификатору

### Запрос:

```http
GET /api/v1/users/{id}
```

### Ответ:

```http
HTTP/1.1 200 OK
```

**_Content-Type: application/json_**

```json
{
  "id": 123,
  "first_name": "UpdatedFirstName",
  "last_name": "UpdatedLastName",
  "date_of_birth": "1990-01-02",
  "phone_number": "89123456789",
  "email": "john.doe@example.com",
  "registration_date": "2023-11-19T14:27:55",
  "subscription_end_date": "2044-11-23T14:31:11",
  "role": "USER",
  "active": true
}
```

## 4. Получение пользователя по email

### Запрос:

```http
GET /api/v1/users/by-email?email=john.doe@example.com
```

### Ответ:

```http
HTTP/1.1 200 OK
```

**_Content-Type: application/json_**

```json
{
  "id": 123,
  "first_name": "UpdatedFirstName",
  "last_name": "UpdatedLastName",
  "date_of_birth": "1990-01-02",
  "phone_number": "89123456789",
  "email": "john.doe@example.com",
  "registration_date": "2023-11-19T14:27:55",
  "subscription_end_date": "2044-11-23T14:31:11",
  "role": "USER",
  "active": true
}
```

## 5. Удаление пользователя

### Запрос:

```http
DELETE /api/v1/users/delete/{id}
```

### Ответ:

```http
HTTP/1.1 200 OK
Content-Type: text/plain

User with id 123 deleted.
```

## 6. Получение всех пользователей

### Запрос:

```http
GET /api/v1/users/all
```

### Ответ:

```http
HTTP/1.1 200 OK
```

**_Content-Type: application/json_**

```json
[
  {
    "id": 123,
    "first_name": "UpdatedFirstName",
    "last_name": "UpdatedLastName",
    "date_of_birth": "1990-01-02",
    "phone_number": "89123456789",
    "email": "john.doe@example.com",
    "registration_date": "2023-11-19T14:27:55",
    "subscription_end_date": "2044-11-23T14:31:11",
    "role": "USER",
    "active": true
  }
  // Другие пользователи...
]
```

## 7. Аутентификация пользователя

### Запрос:

```http
POST /api/v1/users/authenticate
```

**_Content-Type: application/json_**

```json
{
  "phone_number": "89123456789",
  "password": "securePassword"
}
```

### Ответ:

```http
HTTP/1.1 200 OK
```

**_Content-Type: application/json_**

```json
{
  "id": 123,
  "first_name": "UpdatedFirstName",
  "last_name": "UpdatedLastName",
  "date_of_birth": "1990-01-02",
  "phone_number": "89123456789",
  "email": "john.doe@example.com",
  "registration_date": "2023-11-19T14:27:55",
  "subscription_end_date": "2044-11-23T14:31:11",
  "role": "USER",
  "active": true
}
```

## 8. Обновление пароля пользователя

### Запрос:

```http
PUT /api/v1/users/password/{id}
```

**_Content-Type: application/json_**

```json
{
  "password": "securePassword",
  "password_new": "newSecurePassword"
}
```

### Ответ:

```http
HTTP/1.1 200 OK
Content-Type: application/json
```

