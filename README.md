# Проект "Управление пользователями"

Этот проект представляет собой простой микросервис для управления пользователями. Он включает в себя RESTful API для добавления, получения и удаления пользователей.

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

## Запуск приложения без использования Maven:
1. Клонируйте репозиторий:

   ```bash
   git clone https://github.com/Kulik129/RegistrationApplication.git
   ```

2. Установите Java командой:

   ```bash
   sudo apt install openjdk-17-jre-headless
   ```
3. Установите Javac командой:

   ```bash
      apt install openjdk-17-jdk-headless
      ```

4. Обновите командой:

   ```bash
   sudo update
   ```

5. Перейдите в каталог проекта:

   ```bash
   cd out/artifacts/registration_jar
   ```

6. Запустите приложение с помощью команды java -jar ModuleStructure.jar:
    ```bash
      java -jar registration.jar
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