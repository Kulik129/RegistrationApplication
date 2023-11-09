package ru.kulik.registration.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kulik.registration.model.User;
import ru.kulik.registration.service.UserService;

import java.net.URI;
import java.util.List;

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
    public ResponseEntity<Void> addUser(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String dateOfBirth,
            @RequestParam String email,
            @RequestParam String password
    ) {
        User user = new User(name, surname, dateOfBirth, email, password);
        userService.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Найденный пользователь или null, если не найден.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        User user = userService.getUser(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя для удаления.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        if (userService.getUser(id) != null) {
            userService.delete(id);
            return ResponseEntity.ok("User with id "+ id + " deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + id + " not found.");
        }
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        if (!users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

