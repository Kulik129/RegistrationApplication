# Проект "Управление пользователями"

Этот проект представляет собой простой микросервис для управления пользователями. Он включает в себя RESTful API для добавления, получения и удаления пользователей.

## Описание

### Технологии

Проект разработан с использованием следующих технологий:

- Java 17
- Spring Framework (Spring Boot) 3.1.5
- Spring Data JPA
- RESTful API
- MySQL
- JUnit 5
- Maven

### Компоненты

Проект состоит из следующих компонентов:

- `UserController`: Контроллер для обработки HTTP-запросов, связанных с пользователями.
- `UserService`: Интерфейс для управления пользователями.
- `UserServiceImpl`: Реализация интерфейса UserService, включая методы для сохранения, получения и удаления пользователей.
- `User`: Модель пользователя, представляющая собой сущность в базе данных.
- `UserRepository`: Интерфейс репозитория для сущностей User, расширяющий JpaRepository.
- `UserValidator`: Класс валидатора для объектов User, проверяющий уникальность email и номера телефона.
- `ApiValidationError`: Представляет детали ошибки валидации для ответов API.
- `CorsConfig`: Класс конфигурации для обработки междоменных запросов (CORS).
- `application.properties`: Файл конфигурации для настройки приложения.

## Установка и запуск

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

## Использование API

### Добавление пользователя


Чтобы добавить нового пользователя, выполните POST-запрос на `/api/v1/users/add`. Передайте JSON-тело с данными:

- `name` (Имя пользователя)
- `surname` (Фамилия пользователя)
- `dateOfBirth` (Дата рождения пользователя)
- `email` (Email пользователя)
- `phoneNumber` (Номер телефона пользователя)
- `password` (Пароль пользователя)
- `registrationDate` (Дата регистрации пользователя)

```json
{
  "name": "John",
  "surname": "Doe",
  "dateOfBirth": "29.10.1999",
  "email": "doe.john@example.com",
  "phoneNumber": "89235431231",
  "password": "secret",
  "registrationDate": "2023-11-14T15:30:00"
}
```

## Обновление данных пользователя

Чтобы обновить данные пользователя, выполните PUT-запрос на `/api/v1/users/update-user-info/{id}`, где `{id}` - идентификатор пользователя. Передайте JSON-тело с обновленными данными:

```json
{
  "name": "NewName",
  "surname": "NewSurname",
  "dateOfBirth": "29.12.1999",
  "phoneNumber": "89111111111"
}
```

**Пример запроса:**
```http
PUT http://localhost:8080/api/v1/users/update-user-info/1
Content-Type: application/json

{
  "name": "NewName",
  "surname": "NewSurname",
  "dateOfBirth": "29.12.1999",
  "phoneNumber": "89111111111"
}
```

## Получение пользователя по идентификатору

Чтобы получить информацию о пользователе по идентификатору, выполните GET-запрос на `/api/v1/users/{id}`, где `{id}` - идентификатор пользователя.

**Пример запроса:**
```http
GET http://localhost:8080/api/v1/users/1
```

## Получение пользователя по email

Чтобы получить пользователя по email, выполните GET-запрос на `/api/v1/users/by-email` с параметром `email`.

**Пример запроса:**
```http
GET http://localhost:8080/api/v1/users/by-email?email=john.doe@example.com
```

## Получение пользователя по номеру телефона

Чтобы получить пользователя по номеру телефона, выполните GET-запрос на `/api/v1/users/by-phone-number` с параметром `phoneNumber`.

**Пример запроса:**
```http
GET http://localhost:8080/api/v1/users/by-phone-number?phoneNumber=89111111111
```

## Удаление пользователя по идентификатору

Чтобы удалить пользователя по идентификатору, выполните DELETE-запрос на `/api/v1/users/delete/{id}`, где `{id}` - идентификатор пользователя.

**Пример запроса:**
```http
DELETE http://localhost:8080/api/v1/users/delete/1
```

## Получение списка всех пользователей

Чтобы получить список всех пользователей, выполните GET-запрос на `/api/v1/users/all`.

**Пример запроса:**
```http
GET http://localhost:8080/api/v1/users/all
```

**Примечание:** В `application.properties`, `allowed.origins=` поставьте хост на фактический адрес вашего приложения с которого будут отправляться запросы к этому API, например: `allowed.origins=http://localhost:8090`.