package ru.kulik.registration.exception;

public class SubscriptionException extends ApiException{
    /**
     * Конструктор класса ApiException.
     *
     * @param message   Сообщение об ошибке.
     * @param errorCode Код ошибки.
     */
    public SubscriptionException(String message, String errorCode) {
        super(message, errorCode);
    }
}
