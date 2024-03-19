# RESTful API для управления пользователями


**_Этот проект представляет собой микросервис для управления пользователями, предоставляющий RESTful API со следующими функциональностями:_**  📃


> - Регистрация нового пользователя.
> - Обновление информации о пользователе.
> - Получение данных пользователя по его идентификатору.
> - Получение пользователя по адресу электронной почты.
> - Получение пользователя по номеру телефона.
> - Удаление учетной записи пользователя.
> - Получение списка всех пользователей.
> - Аутентификация пользователя по номеру телефона и паролю.
> - Смена пароля пользователя.
> - Обновление даты окончания подписки.
> - Назначение пользователя ролью администратора или обычного пользователя.
> - Блокировка учетной записи пользователя.

### Технологии

⚙️ **_Проект разработан с использованием следующих технологий:_**

> - Java 17
> - Spring Framework (Spring Boot) 3.1.5
> - Spring Data JPA
> - Spring Security
> - Liquibase
> - PostgreSQL
> - Hibernate
> - RESTful API
> - JUnit 5
> - Maven
> - Swagger
> - Aspects


---
### **Важно для application.properties:**  🚨
> 
> `allowed.origins=` поставьте хост на фактический адрес вашего приложения с
> которого будут отправляться запросы к этому API, например: `allowed.origins=http://localhost:8090` или * если хотите разрешить досту с любого хоста.

> 
> 🦈 Для использования диалекта MySQL подставьте к: `spring.jpa.properties.hibernate.dialect=` эту настройку `org.hibernate.dialect.MySQLDialect`, 
> 
> 
> 🐘 Для использования диалекта PostgreSQL подставьте к: `spring.jpa.properties.hibernate.dialect=` эту настройку `org.hibernate.dialect.PostgreSQLDialect` 

> 📧 Для отправки письма на почту пользователю, с восстановленным паролем, к `spring.mail.username=` поставьте адрес настроенной почты, с которой будет отправка писем, 
> к `spring.mail.password=` подставьте пароль от почтового ящика. 

## Установка и запуск 💽


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

## Установка Java 17 на Linux: ☕️ 🐧

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

# _Ниже приведены примеры запросов для каждого эндпоинта._

> ## 🫧 1. Добавление нового пользователя

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

___

> ## 🫧 2. Обновление данных пользователя

### Запрос:

```http
PUT /api/v1/users/update-user-info/123
```

**_Content-Type: application/json_**

```json
{
  "first_name": "UpdatedFirstName",
  "last_name": "UpdatedLastName",
  "date_of_birth": "1990-01-02"
}
```

___

> ## 🫧 3. Получение пользователя по идентификатору

### Запрос:

```http
GET /api/v1/users/123
```


___


> ## 🫧 4. Получение пользователя по email

### Запрос:

```http
GET /api/v1/users/by-email?email=john.doe@example.com
```

___


> ## 🫧 5. Удаление пользователя

### Запрос:

 ```http
 DELETE /api/v1/users/delete/1
 ```

___

> ## 🫧 6. Получение всех пользователей

### Запрос:

```http
GET /api/v1/users/all
```



**_Content-Type: application/json_**

```json
[
  {
    "id": 123,
    "first_name": "John",
    "last_name": "Doe",
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

___

> ## 🫧 7. Аутентификация пользователя

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

___

> ## 🫧 8. Обновление пароля пользователя

### Запрос:

```http
PUT /api/v1/users/password/123
```


```json
{
  "password": "securePassword",
  "password_new": "newSecurePassword"
}
```

___

> ## 🫧 9. Обновление роли пользователя, например ADMIN

```http
PUT /api/v1/users/role/1?param=true
```

___

> ## 🫧 10. Блокировка пользователя

```http
PUT /api/v1/users/active/1?param=false
```

___

> ## 🫧 11. Обновление окончания даты подписки
 ```http 
PUT /api/v1/users/subscription/1?dateTime=2023-12-31T23:59:59
```

___

> ## 🫧 12. Восстановление пароля
 ```http 
PUT /api/v1/users/update-password/1
```
