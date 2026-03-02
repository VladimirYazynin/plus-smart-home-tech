package ru.practicum.commerce.shoppingstore.exception;

public class ProductInShoppingCartLowQuantityInWarehouse extends RuntimeException {

    public ProductInShoppingCartLowQuantityInWarehouse(String message) {
        super(message);
    }
}
