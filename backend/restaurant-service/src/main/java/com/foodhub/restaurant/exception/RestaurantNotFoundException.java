package com.foodhub.restaurant.exception;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(Long id) {
        super("Restaurant not found with id: " + id);
    }
    
    public RestaurantNotFoundException(String name) {
        super("Restaurant not found with name: " + name);
    }
}
