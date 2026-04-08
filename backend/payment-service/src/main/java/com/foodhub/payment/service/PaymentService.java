package com.foodhub.payment.service;

import com.foodhub.payment.entity.Payment;
import com.foodhub.payment.exception.PaymentNotFoundException;
import com.foodhub.payment.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    
    private final PaymentRepository paymentRepository;
    
    @Value("${stripe.secret.key:sk_test_dummy}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public String createPaymentSession(String bookingId, Double amount) throws Exception {
        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("http://localhost:4200/payment-success?orderId=" + bookingId)
            .setCancelUrl("http://localhost:4200/payment")
            .addLineItem(SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("inr")
                    .setUnitAmount((long)(amount * 100))
                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName("Food Order")
                        .build())
                    .build())
                .build())
            .build();

        Session session = Session.create(params);
        
        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setStripeSessionId(session.getId());
        payment.setAmount(amount);
        payment.setPaymentStatus("PENDING");
        paymentRepository.save(payment);
        
        return session.getUrl();
    }

    public Payment getPaymentStatus(String sessionId) {
        return paymentRepository.findByStripeSessionId(sessionId)
            .orElseThrow(() -> new PaymentNotFoundException(sessionId));
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
