package com.example.ecommerce.order.observer;

import com.example.ecommerce.order.model.Order;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderStatusNotifier {

    private static final List<OrderStatusObserver> observers = new ArrayList<>();

    public static void addObserver(OrderStatusObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OrderStatusObserver observer) {
        observers.remove(observer);
    }

    public static void notifyStatusChange(Order order) {
        for (OrderStatusObserver observer : observers) {
            observer.onStatusChanged(order);
        }
    }
}
