package com.example.ecommerce.order.command;

import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.observer.OrderStatusNotifier;
import com.example.ecommerce.order.repository.OrderRepository;

public class PlaceOrderCommand implements OrderCommand {

    private final Order order;
    private final OrderRepository repository;

    public PlaceOrderCommand(Order order, OrderRepository repository) {
        this.order = order;
        this.repository = repository;
    }

    @Override
    public void execute() {
        order.setStatus("PLACED");
        repository.save(order);
        OrderStatusNotifier notifier = new OrderStatusNotifier();  // Or use dependency injection
        notifier.notifyStatusChange(order);    }
}
