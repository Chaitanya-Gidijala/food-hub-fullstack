package com.foodhub.restaurant.exception;

public class MenuItemNotFoundException extends RuntimeException {
    public MenuItemNotFoundException(Long id) {
        super("Menu item not found with id: " + id);
    }
}
