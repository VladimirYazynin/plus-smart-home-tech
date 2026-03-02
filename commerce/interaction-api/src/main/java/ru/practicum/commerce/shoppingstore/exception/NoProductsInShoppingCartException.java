package ru.practicum.commerce.shoppingstore.exception;

public class NoProductsInShoppingCartException extends RuntimeException {

    public NoProductsInShoppingCartException(String message) {
        super(message);
    }
}
