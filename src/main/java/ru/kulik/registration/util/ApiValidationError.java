package ru.kulik.registration.util;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.FieldError;

@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApiValidationError {
    /**
     * Поле, связанное с ошибкой валидации.
     */
    private String field;
    /**
     * Сообщение об ошибке, описывающее проблему валидации.
     */
    private String message;

    public static ApiValidationError mapToResponse(FieldError fieldError) {
        return new ApiValidationError(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
