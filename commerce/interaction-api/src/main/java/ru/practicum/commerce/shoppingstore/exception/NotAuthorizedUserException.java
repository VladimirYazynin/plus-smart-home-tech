package ru.practicum.commerce.shoppingstore.exception;

public class NotAuthorizedUserException extends RuntimeException {

    public NotAuthorizedUserException(String message) {
        super(message);
    }
}
