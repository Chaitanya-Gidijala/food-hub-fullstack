package com.foodhub.payment.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "payments")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String bookingId;
    private String stripeSessionId;
    private Double amount;
    private String paymentStatus;
}
