package ru.kulik.registration.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

public class ValidationUtil {
    /**
     * Обработка ошибок валидации и формирование ответа с информацией о невалидных полях.
     *
     * @param bindingResult Результат валидации
     * @return Ответ с информацией о невалидных полях
     */
    public static ResponseEntity<?> handleValidationErrors(BindingResult bindingResult) {
        List<ApiValidationError> errors = bindingResult.getFieldErrors()
                .stream()
                .map(ApiValidationError::mapToResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
