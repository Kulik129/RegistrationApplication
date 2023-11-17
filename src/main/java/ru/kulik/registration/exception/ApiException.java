package ru.kulik.registration.exception;

/**
 * Исключение, представляющее общий класс для ошибок в API.
 */
public class ApiException extends RuntimeException {
    /**
     * Код ошибки.
     */
    protected String errorCode;

    /**
     * Конструктор класса ApiException.
     *
     * @param message   Сообщение об ошибке.
     * @param errorCode Код ошибки.
     */
    public ApiException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
