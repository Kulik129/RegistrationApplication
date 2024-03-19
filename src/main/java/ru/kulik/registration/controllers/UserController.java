package ru.kulik.registration.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kulik.registration.DTO.request.UpdateInfo;
import ru.kulik.registration.DTO.request.UpdatePassword;
import ru.kulik.registration.DTO.request.UserAuthenticate;
import ru.kulik.registration.DTO.request.UserRequest;
import ru.kulik.registration.DTO.response.UserResponse;
import ru.kulik.registration.model.User;
import ru.kulik.registration.service.TokenService;
import ru.kulik.registration.service.UserService;
import ru.kulik.registration.util.UserValidator;
import ru.kulik.registration.util.ValidationUtil;

import java.util.List;

/**
 * Контроллер для управления пользователями.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserValidator userValidator;
    private final ModelMapper modelMapper;
    private final TokenService tokenService;


    @Operation(
            summary = "Добавляет нового пользователя.",
            description = "Принимает данные нового пользователя в формате JSON. Возвращает JWT token с данными пользователя.")
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserRequest user, BindingResult bindingResult) {
        userValidator.validate(modelMapper.map(user, User.class), bindingResult);
        if (bindingResult.hasErrors()) {
            return ValidationUtil.handleValidationErrors(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenService.createUser(user));
    }

    @Operation(
            summary = "Обновляет данные о пользователе. Имя, фамилию, дату рождения.",
            description = "Принимает идентификатор пользователя и данные для обновления в формате JSON. Возвращает ResponseEntity с обновленным пользователем и статусом OK.")
    @PutMapping("/update-user-info/{id}")
    public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody @Valid UpdateInfo updateInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ValidationUtil.handleValidationErrors(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.OK).body(tokenService.updateUserInfo(id, updateInfo));
    }

    @Operation(
            summary = "Получает пользователя по идентификатору.",
            description = "Принимает идентификатор пользователя. Возвращает ResponseEntity с пользователем и статусом OK.")
    @GetMapping("/{id}")
    public ResponseEntity<String> getUserById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(tokenService.getUserByID(id));
    }

    @Operation(
            summary = "Получает пользователя по email.",
            description = "Принимает email пользователя. Возвращает JWT token с данными пользователя и статусом OK.")
    @GetMapping("/by-email")
    public ResponseEntity<String> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.OK).body(tokenService.getUserByEmail(email));
    }

    @Operation(
            summary = "Получает пользователя по номеру телефона.",
            description = "Принимает номер телефона пользователя. Возвращает JWT token с данными пользователя и статусом OK.")
    @GetMapping("/by-phone-number")
    public ResponseEntity<String> getUserByPhoneNumber(@RequestParam String phone) {
        return ResponseEntity.status(HttpStatus.OK).body(tokenService.getUserByPhone(phone));
    }

    @Operation(
            summary = "Удаляет пользователя по идентификатору.",
            description = "Принимает идентификатор пользователя. Возвращает ответ со статусом OK и список пользователей.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<List<UserResponse>> deleteUser(@PathVariable long id) {
        userService.deleteUserByID(id);
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @Operation(
            summary = "Получает всех пользователей.",
            description = "Возвращает ResponseEntity со списком пользователей и статусом OK.")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @Operation(summary = "Аутентификация пользователя по номеру телефона и паролю.",
            description = "Возвращает JWT token с данными пользователя и статусом OK.")
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody UserAuthenticate userAuthenticate) {
        return ResponseEntity.status(HttpStatus.OK).body(tokenService.authenticate(userAuthenticate));
    }

    @Operation(
            summary = "Обновляет пароль пользователю по его ID.",
            description = "Возвращает DTO с данными пользователя и статусом OK.")

    @PutMapping("/password/{id}")
    public ResponseEntity<?> updatePassword(@PathVariable long id, @RequestBody @Valid UpdatePassword request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ValidationUtil.handleValidationErrors(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.OK).body(tokenService.updatePassword(id, request));
    }

    @Operation(
            summary = "Обновляет дату окончания подписки пользователя.",
            description = "Возвращает JWT token с данными пользователя и статусом OK.")
    @PutMapping("/subscription/{id}")
    public ResponseEntity<String> updateSubscription(@PathVariable long id, @RequestParam String dateTime) {
        return ResponseEntity.status(HttpStatus.OK).body(tokenService.updateSubscription(id, dateTime));
    }

    @Operation(
            summary = "Обновляет роль пользователя.",
            description = "Возвращает JWT token с данными пользователя и статусом OK.")
    @PutMapping("/role/{id}")
    public ResponseEntity<String> updateRole(@PathVariable long id, @RequestParam boolean param) {
        return ResponseEntity.status(HttpStatus.OK).body(tokenService.updateRole(id, param));
    }

    @Operation(
            summary = "Блокирует или разблокирует пользователя.",
            description = "Возвращает JWT token с данными пользователя и статусом OK.")
    @PutMapping("/active/{id}")
    public ResponseEntity<String> updateActive(@PathVariable long id, @RequestParam boolean param) {
        return ResponseEntity.status(HttpStatus.OK).body(tokenService.updateActive(id, param));
    }
}

