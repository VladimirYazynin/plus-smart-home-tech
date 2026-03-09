package ru.practicum.commerce.shoppingstore.exception;

public class NoDeliveryFoundException extends RuntimeException {

    public NoDeliveryFoundException(String message) {
        super(message);
    }
}
