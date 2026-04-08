package com.foodhub.restaurant.service;

import com.foodhub.restaurant.entity.MenuItem;
import com.foodhub.restaurant.entity.Restaurant;
import com.foodhub.restaurant.exception.MenuItemNotFoundException;
import com.foodhub.restaurant.exception.RestaurantNotFoundException;
import com.foodhub.restaurant.repository.MenuItemRepository;
import com.foodhub.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private static final Logger logger = LoggerFactory.getLogger(RestaurantService.class);
    
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantByName(String name) {
        return restaurantRepository.findByRestaurantName(name)
                .orElseThrow(() -> new RestaurantNotFoundException(name));
    }

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
    }

    public Restaurant getRestaurantByOwnerId(Long ownerId) {
        return restaurantRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new RestaurantNotFoundException("No restaurant found for owner: " + ownerId));
    }

    public Restaurant verifyRestaurant(Long id, String status) {
        Restaurant restaurant = getRestaurantById(id);
        restaurant.setVerificationStatus(status);
        return restaurantRepository.save(restaurant);
    }

    public Restaurant updateRestaurant(String name, Restaurant restaurant) {
        Restaurant existing = getRestaurantByName(name);
        existing.setLocation(restaurant.getLocation());
        existing.setPhone(restaurant.getPhone());
        existing.setEmail(restaurant.getEmail());
        existing.setOpeningHoursJson(restaurant.getOpeningHoursJson());
        return restaurantRepository.save(existing);
    }

    public void deleteRestaurant(String name) {
        Restaurant restaurant = getRestaurantByName(name);
        restaurantRepository.delete(restaurant);
    }

    public MenuItem addMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public List<MenuItem> getMenuItemsByRestaurantName(String name) {
        Restaurant restaurant = getRestaurantByName(name);
        return menuItemRepository.findByRestaurantId(restaurant.getId());
    }

    public MenuItem updateMenuItem(Long id, MenuItem menuItem) {
        MenuItem existing = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException(id));
        existing.setDishName(menuItem.getDishName());
        existing.setDescription(menuItem.getDescription());
        existing.setPrice(menuItem.getPrice());
        existing.setCategoryName(menuItem.getCategoryName());
        existing.setAvailable(menuItem.getAvailable());
        return menuItemRepository.save(existing);
    }

    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }
}
