package ru.kulik.registration.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kulik.registration.DTO.UpdatePasswordDto;
import ru.kulik.registration.DTO.UpdateUserInfoDto;
import ru.kulik.registration.DTO.UserDto;
import ru.kulik.registration.model.User;
import ru.kulik.registration.model.UserRole;
import ru.kulik.registration.service.UserService;
import ru.kulik.registration.util.UserValidator;
import ru.kulik.registration.util.ValidationUtil;

import java.net.URI;
import java.time.LocalDateTime;
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
    private final ModelMapper modelMapper;

    /**
     * Конструктор контроллера.
     *
     * @param userService     Сервис для управления пользователями.
     * @param userValidator
     * @param passwordEncoder
     * @param modelMapper
     */
    @Autowired
    public UserController(UserService userService, UserValidator userValidator, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    /**
     * Добавляет нового пользователя.
     */
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserDto user, BindingResult bindingResult) {
        User userMap = modelMapper.map(user, User.class);
        userValidator.validate(userMap, bindingResult);

        if (bindingResult.hasErrors()) {
            return ValidationUtil.handleValidationErrors(bindingResult);
        }

        userService.createUser(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Обновляет данные о пользователе. Имя, фамилию, дату рождения, номер телефона.
     *
     * @param id      id пользователя.
     * @param request json с данными.
     * @return ResponseEntity с обновленным пользователем и статусом ОК, или NOT_FOUND, если пользователь не найден.
     */
    @PutMapping("/update-user-info/{id}")
    public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody @Valid UpdateUserInfoDto request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ValidationUtil.handleValidationErrors(bindingResult);
        }
        Optional<UserDto> user = userService.getUserByID(id);
        if (user.isPresent()) {
            UserDto userUpdate = user.get();
            userUpdate.setFirstName(request.getFirstName());
            userUpdate.setLastName(request.getLastName());
            userUpdate.setDateOfBirth(request.getDateOfBirth());

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
    public ResponseEntity<UserDto> getUserById(@PathVariable long id) {
        Optional<UserDto> user = userService.getUserByID(id);

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
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        Optional<UserDto> user = userService.getUserByEmail(email);

        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Получает пользователя по номеру телефона.
     *
     * @param phone номер тел. пользователя.
     * @return ResponseEntity с пользователем и статусом ОК, или NOT_FOUND, если пользователь не найден.
     */
    @GetMapping("/by-phone-number")
    public ResponseEntity<UserDto> getUserByPhoneNumber(@RequestParam String phone) {
        Optional<UserDto> user = userService.getUserByPhone(phone);

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
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();

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
    public ResponseEntity<UserDto> authenticate(@RequestBody UserDto userAuthenticate) {
        Optional<UserDto> user = userService.getUserByPhone(userAuthenticate.getPhoneNumber());
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
    @PutMapping("/password/{id}")
    public ResponseEntity<?> updatePassword(@PathVariable long id, @RequestBody @Valid UpdatePasswordDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ValidationUtil.handleValidationErrors(bindingResult);
        }
        Optional<UserDto> user = userService.getUserByID(id);
        if (passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            user.get().setPassword(passwordEncoder.encode(request.getPasswordNew()));
            userService.save(user.get());
            return ResponseEntity.ok(user.get());
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Обновление даты окончания подписки пользователя.
     *
     * @param id       Идентификатор пользователя.
     * @param dateTime дата для обновления окончания подписки.
     * @return ResponseEntity с пользователем и статусом OK.
     */
    @PutMapping("/subscription/{id}")
    public ResponseEntity<UserDto> updateSubscription(@PathVariable long id, @RequestParam LocalDateTime dateTime) {
        Optional<UserDto> user = userService.getUserByID(id);
        if (user.isPresent()) {
            user.get().setSubscriptionEndDate(dateTime);
            userService.save(user.get());
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Обновление роли пользователя на admin.
     *
     * @param id   Идентификатор пользователя.
     * @return ResponseEntity с пользователем и статусом OK в случае успеха, иначе BAD_REQUEST.
     */
    @PutMapping("/role/{id}")
    public ResponseEntity<UserDto> updateRole(@PathVariable long id, @RequestParam boolean param) {
        Optional<UserDto> user = userService.getUserByID(id);
        if (user.isPresent()) {
            if (param) {
                user.get().setRole(UserRole.ADMIN);
                userService.save(user.get());
            } else {
                user.get().setRole(UserRole.USER);
                userService.save(user.get());
            }
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Блокировка пользователя.
     * @param id id пользователя.
     * @return ResponseEntity с пользователем и статусом OK в случае успеха, иначе BAD_REQUEST.
     */
    @PutMapping("/active/{id}")
    public ResponseEntity<UserDto> updateActive(@PathVariable long id, @RequestParam boolean param) {
        Optional<UserDto> user = userService.getUserByID(id);
        if (user.isPresent()) {
            if (param) {
                user.get().setActive(true);
                userService.save(user.get());
            } else {
                user.get().setActive(false);
                userService.save(user.get());
            }
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}

