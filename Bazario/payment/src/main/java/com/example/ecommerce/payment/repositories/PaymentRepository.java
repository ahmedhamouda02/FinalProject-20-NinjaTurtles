package com.example.ecommerce.payment.repositories;


import com.example.ecommerce.payment.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
