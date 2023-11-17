# Проект "Управление пользователями"

Этот проект представляет собой простой микросервис для управления пользователями. Он включает в себя RESTful API для добавления, получения и удаления пользователей.

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
- `UserServiceImpl`: Реализация интерфейса UserService, включая методы для сохранения, получения и удаления пользователей.
- `UserValidator`: Класс валидатора для объектов User, проверяющий уникальность email и номера телефона.
- `application.properties`: Файл конфигурации для настройки приложения.

---

## Установка и запуск
**Примечание:** В `application.properties`, `allowed.origins=` поставьте хост на фактический адрес вашего приложения с которого будут отправляться запросы к этому API, например: `allowed.origins=http://localhost:8090`.


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

## Использование API

### Добавление пользователя


Чтобы добавить нового пользователя, выполните POST-запрос на `/api/v1/users/add`. Передайте JSON-тело с данными:

- `email` (Email пользователя)
- `password` (Пароль пользователя)
- `role` (Роль пользователя. USER или ADMIN) 
- `active` (Активность пользователя)
- `first_name` (Имя пользователя)
- `last_name` (Фамилия пользователя)
- `date_of_birth` (Дата рождения пользователя)
- `phone_number` (Номер телефона пользователя)
- `registration_date` (Дата регистрации пользователя)
- `subscription_end_date` (Дата окончания подписки)

```json
{
   "email": "doe@example.ru",
   "password": "4321",
   "role": "USER",
   "active": true,
   "first_name": "Doe",
   "last_name": "John",
   "date_of_birth": "01.01.1990",
   "phone_number": "89234321232",
   "registration_date": "2023-11-14T15:30:00",
   "subscription_end_date": "2023-11-14T15:30:00"
}
```

---

## Обновление данных пользователя

Чтобы обновить данные пользователя, выполните PUT-запрос на `/api/v1/users/update-user-info/{id}`, где `{id}` - идентификатор пользователя. Передайте JSON-тело с обновленными данными:

```json
{
  "first_name": "NewName",
  "last_name": "NewSurname",
  "date_of_birth": "29.12.1999",
  "phone_number": "89111111111"
}
```

**Пример запроса:**
```http
PUT http://localhost:8080/api/v1/users/update-user-info/1
```

---

## Получение пользователя по идентификатору

Чтобы получить информацию о пользователе по идентификатору, выполните GET-запрос на `/api/v1/users/{id}`, где `{id}` - идентификатор пользователя.

**Пример запроса:**
```http
GET http://localhost:8080/api/v1/users/1
```

```json
{
    "id": 1,
    "email": "tomas@mail.ru",
    "password": "$2a$10$HOSOywEa4/JLN8RLXbU7HufBpfMEzhQzUMT7PRw.SygZrVxDRt75m",
    "role": "USER",
    "active": true,
    "first_name": "Tomas",
    "last_name": "Shelby",
    "date_of_birth": "15.06.1915",
    "phone_number": "89111111111",
    "registration_date": "2023-11-14T15:30:00",
    "subscription_end_date": "2023-12-14T15:30:00"
}
```

---

## Получение пользователя по email

Чтобы получить пользователя по email, выполните GET-запрос на `/api/v1/users/by-email`. Передайте JSON-тело с данными:

**Пример запроса:**
```json
{
   "email": "tomas@mail.ru"
}
```

---

## Получение пользователя по номеру телефона

Чтобы получить пользователя по номеру телефона, выполните GET-запрос на `/api/v1/users/by-phone-number` . Передайте JSON-тело с данными:

**Пример запроса:**
```json
{
   "phone_number": "89111111111"
}
```

---

## Удаление пользователя по идентификатору

Чтобы удалить пользователя по идентификатору, выполните DELETE-запрос на `/api/v1/users/delete/{id}`, где `{id}` - идентификатор пользователя.

**Пример запроса:**
```http
DELETE http://localhost:8080/api/v1/users/delete/1
```

---

## Получение списка всех пользователей

Чтобы получить список всех пользователей, выполните GET-запрос на `/api/v1/users/all`.

**Пример запроса:**
```http
GET http://localhost:8080/api/v1/users/all
```

---

## Аутентификация по номеру телефона

Чтобы получить пользователя выполните POST-запрос на `/api/v1/users/authenticate` Передайте JSON-тело с номером и паролем:
**Пример запроса:**
```http
POST http://localhost:8080/api/v1/users/authenticate
```
```json
{
    "phone_number": "89234321232",
    "password": "4321"
}
```

---
## Обновление пароля
Чтобы обновить пароль выполните PUT-запрос на `/update-password/{id}`, где `{id}`- идентификатор пользователя. Передайте JSON-тело с прежним паролем и обновленным:
**Пример запроса:**
```http
PUT http://localhost:8080/api/v1/users/update-password/1
```
```json
{
   "password": "111111",
   "password_new": "222222"
}
```

---
## Обновить дату подписки
Чтобы обновить дату подписки выполните PUT-запрос на `/update-subscription/{id}`, где `{id}`- идентификатор пользователя. Передайте JSON-тело с обновленной датой окончания подписки:
**Пример запроса:**
```http
PUT http://localhost:8080/api/v1/users/update-subscription/1
```
```json
{
   "subscription_end_date": "2024-02-19T15:39:10"
}
```

---

## Обновить роль пользователя
По умолчанию роль пользователя null. 
Чтобы установить пользователя как USER или ADMIN выполните PUT-запрос на `/update-role/{id}`, где `{id}`- идентификатор пользователя. Передайте JSON-тело с обновленной датой окончания подписки:
**Пример запроса:**
```http
PUT http://localhost:8080/api/v1/users/update-role/1
```
```json
{
   "role": "USER"
}
```

---
