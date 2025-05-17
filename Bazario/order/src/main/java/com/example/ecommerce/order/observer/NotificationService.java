package com.example.ecommerce.order.observer;

import com.example.ecommerce.order.model.Order;

public class NotificationService implements OrderStatusObserver {
    @Override
    public void onStatusChanged(Order order) {
        System.out.println("Notify user: Order " + order.getId() + " is now " + order.getStatus());
    }
}
