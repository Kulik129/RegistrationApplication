package ru.kulik.registration.controllers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.kulik.registration.util.ApiValidationError;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер обработки ошибок.
 */
@ControllerAdvice
@RestController
public class RestExceptionController {
    /**
     * Поле в БД с email пользователей.
     */
    private final String fieldEmail = "user.UK_ob8kqyqqgmefl0aco34akdtpe";
    /**
     * Поле в БД с номерами пользователей.
     */
    private final String fieldPhoneNumber = "user.UK_4bgmpi98dylab6qdvf9xyaxu4";

    /**
     * Метод обработки не валидных значений для БД.
     *
     * @param ex объект класса MethodArgumentNotValidException.
     * @return Возвращает пользователю JSON содержащий сообщение с невалидным полем и сообщением.
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
     * Метод обработки не валидных значений с email и phoneNumber для БД.
     *
     * @param ex объект класса SQLIntegrityConstraintViolationException.
     * @return Возвращает пользователю JSON содержащий сообщение с невалидным полем и сообщением.
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
     * Метод обработки не валидных значений при update данных.
     *
     * @param ex объект класса ConstraintViolationException.
     * @return Возвращает пользователю JSON содержащий сообщение с невалидным полем и сообщением.
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
}

