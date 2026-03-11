package ru.practicum.commerce.shoppingstore.exception;

public class NotEnoughInfoInOrderToCalculateException extends RuntimeException {

    public NotEnoughInfoInOrderToCalculateException(String message) {
        super(message);
    }
}
