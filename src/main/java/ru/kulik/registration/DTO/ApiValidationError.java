package ru.kulik.registration.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiValidationError {
    /**
     * Поле, связанное с ошибкой валидации.
     */
    private String field;
    /**
     * Сообщение об ошибке, описывающее проблему валидации.
     */
    private String message;
}
