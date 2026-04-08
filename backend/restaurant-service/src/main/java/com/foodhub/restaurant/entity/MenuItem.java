package com.foodhub.restaurant.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "menu_items")
@Data
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String dishName;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private Double price;
    private String categoryName;
    private Long restaurantId;
    private Boolean available = true;
}
