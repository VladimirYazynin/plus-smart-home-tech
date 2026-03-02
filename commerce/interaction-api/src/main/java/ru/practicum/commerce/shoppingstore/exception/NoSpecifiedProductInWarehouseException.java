package ru.practicum.commerce.shoppingstore.exception;

public class NoSpecifiedProductInWarehouseException extends RuntimeException {

    public NoSpecifiedProductInWarehouseException(String message) {
        super(message);
    }
}
