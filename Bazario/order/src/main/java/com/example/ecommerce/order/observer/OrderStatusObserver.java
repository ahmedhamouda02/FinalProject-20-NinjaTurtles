package com.example.ecommerce.order.observer;

import com.example.ecommerce.order.model.Order;

public interface OrderStatusObserver {
    void onStatusChanged(Order order);
}
