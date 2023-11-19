package ru.kulik.registration.cors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import ru.kulik.registration.util.ApiValidationError;
import ru.kulik.registration.exception.UserNotFoundException;

/**
 * Контроллер обработки ошибок.
 */
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {


    /**
     * Глобальный обработчик исключений для случаев, когда пользователь не найден.
     *
     * @param ex Исключение UserNotFoundException.
     * @return ResponseEntity с сообщением об ошибке и статусом NOT_FOUND.
     */
    @ExceptionHandler
    public ResponseEntity<ApiValidationError> userNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(new ApiValidationError("", ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}

