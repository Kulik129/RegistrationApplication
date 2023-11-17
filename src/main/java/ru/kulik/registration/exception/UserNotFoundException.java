package ru.kulik.registration.exception;

/**
 * Исключение, представляющее ошибку отсутствия пользователя.
 */
public class UserNotFoundException extends ApiException{
    public UserNotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }
}
