package com.example.ecommerce.order.service;

import com.example.ecommerce.order.DTO.ItemDTO;
import com.example.ecommerce.order.DTO.PaymentsDTO;
import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.repository.OrderRepository;
import com.example.ecommerce.order.command.*;
import com.example.ecommerce.order.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public List<Order> getOrderHistory(UUID userId, Optional<String> status) {
        return status.map(s -> repository.findByUserIdAndStatus(userId, s))
                .orElseGet(() -> repository.findByUserId(userId));
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_QUEUE)
    public void receivePaymentMessage(PaymentsDTO payment) {
         Double amount= payment.getAmount();
         Long userId = payment.getUserId();
         List<ItemDTO> items = payment.getItems();
        // *** Add your business logic here to process the payment DTO ***
        // For example:
        // - Update the corresponding order status based on payment.getOrderId() and payment.getStatus()
        // - Log the payment details
        // - Trigger further actions within the order service
    }
    public void placeOrder(Order order) {
        OrderCommand placeCommand = new PlaceOrderCommand(order, repository);
        placeCommand.execute();
    }

    public void cancelOrder(Long orderId) {
        OrderCommand cancelCommand = new CancelOrderCommand(orderId, repository);
        cancelCommand.execute();
    }
    public boolean updateOrder(Long orderId, Order updatedOrder) {
        // Fetch the order by ID
        Optional<Order> orderOpt = repository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            // Update the fields
            order.setStatus(updatedOrder.getStatus());
            order.setDetails(updatedOrder.getDetails());
            // Save the updated order back to the repository
            repository.save(order);
            return true;
        }
        return false; // If order not found
    }
    public boolean deleteOrder(Long orderId) {
        // Find the order by ID
        Optional<Order> orderOpt = repository.findById(orderId);
        if (orderOpt.isPresent()) {
            // Delete the order
            repository.delete(orderOpt.get());
            return true;
        }
        return false; // If order not found
    }

    public Order getOrderById(Long orderId) {
        return repository.findById(orderId).orElse(null);
    }



}
