package ru.kulik.registration.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kulik.registration.model.User;
import ru.kulik.registration.service.UserService;

/**
 * Контроллер для управления пользователями.
 */
@RestController
public class UserController {
    private final UserService userService;

    /**
     * Конструктор контроллера.
     *
     * @param userService Сервис для управления пользователями.
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Добавляет нового пользователя.
     *
     * @param name     Имя пользователя.
     * @param password Пароль пользователя.
     */
    @PostMapping("/add")
    public void addUser(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String dateOfBirth,
            @RequestParam String email,
            @RequestParam String password
    ) {
        User user = new User(name, surname, dateOfBirth, email, password);
        userService.save(user);
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Найденный пользователь или null, если не найден.
     */
    @GetMapping("/get")
    public User getUser(@RequestParam long id) {
        return userService.getUser(id);
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя для удаления.
     */
    @DeleteMapping("/delete")
    public void deleteUser(@RequestParam long id) {
        userService.delete(id);
    }
}

