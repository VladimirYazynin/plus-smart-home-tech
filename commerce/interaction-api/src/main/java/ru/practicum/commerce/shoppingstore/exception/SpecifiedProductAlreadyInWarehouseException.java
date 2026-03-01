package ru.practicum.commerce.shoppingstore.exception;

public class SpecifiedProductAlreadyInWarehouseException extends RuntimeException {

  public SpecifiedProductAlreadyInWarehouseException(String message) {
        super(message);
  }
}
