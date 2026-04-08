package com.foodhub.restaurant.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "restaurants")
@Data
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String restaurantName;
    
    private Long ownerId;
    private String location;
    private String phone;
    private String email;
    
    @Column(columnDefinition = "TEXT")
    private String openingHoursJson;
    
    private String verificationStatus = "PENDING";
}
