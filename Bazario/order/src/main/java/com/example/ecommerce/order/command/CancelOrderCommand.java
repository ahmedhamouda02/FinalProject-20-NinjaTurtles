package com.example.ecommerce.order.command;

import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.observer.OrderStatusNotifier;
import com.example.ecommerce.order.repository.OrderRepository;

public class CancelOrderCommand implements OrderCommand {

    private final Long orderId;
    private final OrderRepository repository;

    public CancelOrderCommand(Long orderId, OrderRepository repository) {
        this.orderId = orderId;
        this.repository = repository;
    }

    @Override
    public void execute() {
        Order order = repository.findById(orderId).orElseThrow();
        order.setStatus("CANCELLED");
        repository.save(order);
        OrderStatusNotifier notifier = new OrderStatusNotifier();  // Or use dependency injection
        notifier.notifyStatusChange(order);
    }
}
