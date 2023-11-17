package ru.kulik.registration.cors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.kulik.registration.exception.InvalidPasswordException;
import ru.kulik.registration.DTO.ApiValidationError;
import ru.kulik.registration.exception.UserNotFoundException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер обработки ошибок.
 */
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {
    /**
     * Поле в БД с email пользователей.
     */
    private final String fieldEmail = "user.UK_ob8kqyqqgmefl0aco34akdtpe";
    /**
     * Поле в БД с номерами пользователей.
     */
    private final String fieldPhoneNumber = "user.UK_4bgmpi98dylab6qdvf9xyaxu4";

    /**
     * Глобальный обработчик исключений для случаев, когда возникают ошибки валидации аргументов метода.
     *
     * @param ex Исключение MethodArgumentNotValidException.
     * @return ResponseEntity с сообщением об ошибке и статусом BAD_REQUEST.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
        List<ApiValidationError> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            errors.add(new ApiValidationError(fieldName, errorMessage));
        });
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Глобальный обработчик исключений для случаев, когда возникает ошибка нарушения уникальности в базе данных.
     *
     * @param ex Исключение SQLIntegrityConstraintViolationException.
     * @return ResponseEntity с сообщением об ошибке и статусом BAD_REQUEST.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        List<ApiValidationError> errors = new ArrayList<>();

        if (ex.getMessage().contains(fieldEmail)) {
            errors.add(new ApiValidationError("email", "Не уникальное поле email."));
        } else if (ex.getMessage().contains(fieldPhoneNumber)) {
            errors.add(new ApiValidationError("phoneNumber", "Не уникальное поле phone_number."));
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Глобальный обработчик исключений для случаев, когда возникают ограничения (ConstraintViolation).
     *
     * @param ex Исключение ConstraintViolationException.
     * @return ResponseEntity с сообщением об ошибке и статусом BAD_REQUEST.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ApiValidationError> errors = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.add(new ApiValidationError(field, message));
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Глобальный обработчик исключений для случаев, когда пароль недопустим.
     *
     * @param ex Исключение InvalidPasswordException.
     * @return ResponseEntity с сообщением об ошибке и статусом BAD_REQUEST.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Object> invalidPassword(InvalidPasswordException ex) {
        List<ApiValidationError> errors = new ArrayList<>();
        errors.add(new ApiValidationError("password", ex.getMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Глобальный обработчик исключений для случаев, когда пользователь не найден.
     *
     * @param ex Исключение UserNotFoundException.
     * @return ResponseEntity с сообщением об ошибке и статусом NOT_FOUND.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> userNotFound(UserNotFoundException ex) {
        List<ApiValidationError> errors = new ArrayList<>();
        errors.add(new ApiValidationError("ID", ex.getMessage()));
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }
}

