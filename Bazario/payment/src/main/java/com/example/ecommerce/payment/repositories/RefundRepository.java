package com.example.ecommerce.payment.repositories;

import com.example.ecommerce.payment.models.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Long> {
}
