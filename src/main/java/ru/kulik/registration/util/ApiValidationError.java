package ru.kulik.registration.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiValidationError {
    private String field;
    private String message;
}
