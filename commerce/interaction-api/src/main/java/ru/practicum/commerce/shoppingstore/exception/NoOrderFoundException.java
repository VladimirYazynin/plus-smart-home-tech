package ru.practicum.commerce.shoppingstore.exception;

public class NoOrderFoundException extends RuntimeException {

    public NoOrderFoundException(String message) {
        super(message);
    }
}
