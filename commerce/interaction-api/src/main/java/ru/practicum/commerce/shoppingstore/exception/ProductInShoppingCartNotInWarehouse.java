package ru.practicum.commerce.shoppingstore.exception;

public class ProductInShoppingCartNotInWarehouse extends RuntimeException {

    public ProductInShoppingCartNotInWarehouse(String message) {
        super(message);
    }
}
