package com.example.ecommerce.order.repository;

import com.example.ecommerce.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(UUID userId);
    List<Order> findByUserIdAndStatus(UUID userId, String status);
}
