package ru.kulik.registration.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kulik.registration.model.User;
import ru.kulik.registration.service.UserService;
import ru.kulik.registration.util.UserValidator;

import java.net.URI;
import java.util.*;

/**
 * Контроллер для управления пользователями.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор контроллера.
     *
     * @param userService     Сервис для управления пользователями.
     * @param userValidator
     * @param passwordEncoder
     */
    @Autowired
    public UserController(UserService userService, UserValidator userValidator, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Добавляет нового пользователя.
     *
     */
    @PostMapping("/add")
    public ResponseEntity<Void> addUser(
            @Valid @RequestBody User user,
            BindingResult bindingResult
    ) {
        userValidator.validate(user, bindingResult);
        userService.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Обновляет данные о пользователе. Имя, фамилию, дату рождения, номер телефона.
     *
     * @param id          id пользователя.
     * @param updatedUser json с данными.
     * @return ResponseEntity с обновленным пользователем и статусом ОК, или NOT_FOUND, если пользователь не найден.
     */
    @PutMapping("/update-user-info/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable long id,
            @RequestBody User updatedUser,
            BindingResult bindingResult
    ) {
        userValidator.validate(updatedUser, bindingResult);

        Optional<User> user = userService.getUserByID(id);
        if (user.isPresent()) {
            User userUpdate = user.get();
            userUpdate.setFirstName(updatedUser.getFirstName());
            userUpdate.setLastName(updatedUser.getLastName());
            userUpdate.setDateOfBirth(updatedUser.getDateOfBirth());
            userUpdate.setPhoneNumber(updatedUser.getPhoneNumber());

            userService.save(userUpdate);

            return new ResponseEntity<>(userUpdate, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return ResponseEntity с пользователем и статусом ОК, или NOT_FOUND, если пользователь не найден.
     */
    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        Optional<User> user = userService.getUserByID(id);

        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Получает пользователя по email.
     *
     * @param email email пользователя.
     * @return ResponseEntity с пользователем и статусом ОК, или NOT_FOUND, если пользователь не найден.
     */
    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        Optional<User> user = userService.getUserByEmail(email);

        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Получает пользователя по номеру телефона.
     *
     * @param phoneNumber номер тел. пользователя.
     * @return ResponseEntity с пользователем и статусом ОК, или NOT_FOUND, если пользователь не найден.
     */
    @GetMapping("/by-phone-number")
    public ResponseEntity<User> getUserByPhoneNumber(@RequestParam String phoneNumber) {
        Optional<User> user = userService.getUserByPhone(phoneNumber);

        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @return Ответ со статусом ОК, или NOT_FOUND, если пользователь не найден.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {

        if (userService.getUserByID(id) != null) {
            userService.delete(id);
            return ResponseEntity.ok("User with id " + id + " deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + id + " not found.");
        }
    }

    /**
     * Получает всех пользователей.
     *
     * @return ResponseEntity с пользователями и статусом ОК, или NOT_FOUND, если пользователи не найдены.
     */
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        if (!users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<User> authenticate(
            @RequestParam String email,
            @RequestParam String password
    ) {
        Optional<User> userOptional = userService.getUserByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
     }
}

