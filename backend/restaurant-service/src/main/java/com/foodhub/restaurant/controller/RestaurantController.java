package com.foodhub.restaurant.controller;

import com.foodhub.restaurant.entity.MenuItem;
import com.foodhub.restaurant.entity.Restaurant;
import com.foodhub.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping("/restaurants")
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        return ResponseEntity.ok(restaurantService.createRestaurant(restaurant));
    }

    @GetMapping("/restaurants")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/restaurants/{name}")
    public ResponseEntity<Restaurant> getRestaurantByName(@PathVariable String name) {
        return ResponseEntity.ok(restaurantService.getRestaurantByName(name));
    }

    @GetMapping("/restaurants/id/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    @GetMapping("/restaurants/owner/{ownerId}")
    public ResponseEntity<Restaurant> getRestaurantByOwnerId(@PathVariable Long ownerId) {
        return ResponseEntity.ok(restaurantService.getRestaurantByOwnerId(ownerId));
    }

    @PutMapping("/restaurants/{id}/verify")
    public ResponseEntity<Restaurant> verifyRestaurant(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(restaurantService.verifyRestaurant(id, status));
    }

    @PutMapping("/restaurants/{name}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable String name, @RequestBody Restaurant restaurant) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(name, restaurant));
    }

    @DeleteMapping("/restaurants/{name}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable String name) {
        restaurantService.deleteRestaurant(name);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/menu-items")
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem menuItem) {
        return ResponseEntity.ok(restaurantService.addMenuItem(menuItem));
    }

    @GetMapping("/restaurants/{name}/menu-items")
    public ResponseEntity<List<MenuItem>> getMenuItems(@PathVariable String name) {
        return ResponseEntity.ok(restaurantService.getMenuItemsByRestaurantName(name));
    }

    @PutMapping("/menu-items/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id, @RequestBody MenuItem menuItem) {
        return ResponseEntity.ok(restaurantService.updateMenuItem(id, menuItem));
    }

    @DeleteMapping("/menu-items/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        restaurantService.deleteMenuItem(id);
        return ResponseEntity.ok().build();
    }
}
