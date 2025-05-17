package com.example.ecommerce.order.command;

import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.observer.OrderStatusNotifier;
import com.example.ecommerce.order.repository.OrderRepository;

public class UpdateOrderCommand implements OrderCommand {

    private final Long orderId;
    private final Order updatedOrder;
    private final OrderRepository repository;

    public UpdateOrderCommand(Long orderId, Order updatedOrder, OrderRepository repository) {
        this.orderId = orderId;
        this.updatedOrder = updatedOrder;
        this.repository = repository;
    }

    @Override
    public void execute() {
        // Find the order by ID
        Order order = repository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        // Update the fields
        order.setStatus(updatedOrder.getStatus());
        order.setDetails(updatedOrder.getDetails());
        // Update other fields as necessary

        // Save the updated order back to the repository
        repository.save(order);
        OrderStatusNotifier notifier = new OrderStatusNotifier();  // Or use dependency injection
        notifier.notifyStatusChange(order);
    }
}
