package ru.kulik.registration.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kulik.registration.DTO.UserDTO;
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
     */
    @PostMapping("/add")
    public ResponseEntity<String> addUser(@Valid @RequestBody User user, BindingResult bindingResult) {
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
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User updatedUser, BindingResult bindingResult) {
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
     * @param request email пользователя.
     * @return ResponseEntity с пользователем и статусом ОК, или NOT_FOUND, если пользователь не найден.
     */
    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(@RequestBody UserDTO request) {
        Optional<User> user = userService.getUserByEmail(request.getEmail());

        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Получает пользователя по номеру телефона.
     *
     * @param request номер тел. пользователя.
     * @return ResponseEntity с пользователем и статусом ОК, или NOT_FOUND, если пользователь не найден.
     */
    @GetMapping("/by-phone-number")
    public ResponseEntity<User> getUserByPhoneNumber(@RequestBody UserDTO request) {
        Optional<User> user = userService.getUserByPhone(request.getPhoneNumber());

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
        if (userService.getUserByID(id).isPresent()) {
            userService.delete(id);
            return ResponseEntity.ok("User with id " + id + " deleted.");
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

    /**
     * Аутентификация пользователя по номеру телефона и паролю.
     *
     * @param userAuthenticate DTO с данными аутентификации (номер телефона и пароль).
     * @return ResponseEntity с пользователем и статусом OK, или UNAUTHORIZED, если аутентификация не удалась.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<User> authenticate(@RequestBody UserDTO userAuthenticate) {
        Optional<User> user = userService.getUserByPhone(userAuthenticate.getPhoneNumber());

        if (passwordEncoder.matches(userAuthenticate.getPassword(), user.get().getPassword())) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Обновление пароля пользователя.
     *
     * @param id      Идентификатор пользователя.
     * @param request DTO с данными для обновления пароля (старый пароль и новый пароль).
     * @return ResponseEntity с пользователем и статусом OK, или UNAUTHORIZED, если старый пароль не совпадает.
     */
    @PutMapping("/update-password/{id}")
    public ResponseEntity<User> updatePassword(@PathVariable long id, @RequestBody UserDTO request) {
        Optional<User> user = userService.getUserByID(id);

        if (passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            user.get().setPassword(request.getPasswordNew());
            userService.save(user.get());

            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Обновление даты окончания подписки пользователя.
     *
     * @param id   Идентификатор пользователя.
     * @param user DTO с данными для обновления даты окончания подписки.
     * @return ResponseEntity с пользователем и статусом OK.
     */
    @PutMapping("/update-subscription/{id}")
    public ResponseEntity<User> updateSubscription(@PathVariable long id, @RequestBody UserDTO user) {
        Optional<User> userUpdate = userService.getUserByID(id);

        userUpdate.get().setSubscriptionEndDate(user.getSubscriptionEndDate());
        userService.save(userUpdate.get());

        return ResponseEntity.ok(userUpdate.get());
    }

    /**
     * Обновление роли пользователя.
     *
     * @param id   Идентификатор пользователя.
     * @param user DTO с данными для обновления роли.
     * @return ResponseEntity с пользователем и статусом OK.
     */
    @PutMapping("/update-role/{id}")
    public ResponseEntity<User> updateRole(@PathVariable long id, @RequestBody UserDTO user) {
        Optional<User> userUpdate = userService.getUserByID(id);

        userUpdate.get().setRole(user.getRole());
        userService.save(userUpdate.get());

        return ResponseEntity.ok(userUpdate.get());
    }
}

