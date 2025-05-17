package com.example.ecommerce.order.observer;

import com.example.ecommerce.order.model.Order;

public class InventoryService implements OrderStatusObserver {
    @Override
    public void onStatusChanged(Order order) {
        System.out.println("Inventory update for order " + order.getId() + ": " + order.getStatus());
    }
}
