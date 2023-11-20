package ru.kulik.registration.cors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.kulik.registration.exception.SubscriptionException;
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

    @ExceptionHandler
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMessage = "Не валидный аргумент: " + ex.getName() + ". Ожидаемый тип: " + ex.getRequiredType().getSimpleName();
        return new ResponseEntity<>(new ApiValidationError("", errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleInvalidDateException(SubscriptionException ex) {
        return new ResponseEntity<>(new ApiValidationError("",ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {
        String errorMessage = "Произошла неожиданная ошибка.";
        return new ResponseEntity<>(new ApiValidationError(errorMessage, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

