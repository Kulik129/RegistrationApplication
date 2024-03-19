package ru.kulik.registration.exception;

public class IncorrectPasswordException extends ApiException{
    /**
     * Конструктор класса ApiException.
     *
     * @param message   Сообщение об ошибке.
     * @param errorCode Код ошибки.
     */
    public IncorrectPasswordException(String message, String errorCode) {
        super(message, errorCode);
    }
}
