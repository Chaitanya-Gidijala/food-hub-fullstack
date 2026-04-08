package com.foodhub.payment.controller;

import com.foodhub.payment.entity.Payment;
import com.foodhub.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create-session")
    public ResponseEntity<String> createSession(@RequestBody Map<String, Object> request) {
        try {
            String bookingId = request.get("bookingId").toString();
            Double amount = Double.parseDouble(request.get("amount").toString());
            String url = paymentService.createPaymentSession(bookingId, amount);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/status/{sessionId}")
    public ResponseEntity<Payment> getStatus(@PathVariable String sessionId) {
        return ResponseEntity.ok(paymentService.getPaymentStatus(sessionId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}
