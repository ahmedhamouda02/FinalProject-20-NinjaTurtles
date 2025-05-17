package com.example.ecommerce.order.command;

import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.repository.OrderRepository;

public class DeleteOrderCommand implements OrderCommand {

    private final Long orderId;
    private final OrderRepository repository;

    public DeleteOrderCommand(Long orderId, OrderRepository repository) {
        this.orderId = orderId;
        this.repository = repository;
    }

    @Override
    public void execute() {
        // Find the order by ID
        Order order = repository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        // Delete the order from the repository
        repository.delete(order);
    }
}
