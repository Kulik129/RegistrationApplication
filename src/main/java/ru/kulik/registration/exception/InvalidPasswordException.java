package ru.kulik.registration.exception;

/**
 * Исключение, представляющее ошибку валидации недопустимого пароля.
 */
public class InvalidPasswordException extends ApiException {

    public InvalidPasswordException(String message, String errorCode) {
        super(message, errorCode);
    }
}
