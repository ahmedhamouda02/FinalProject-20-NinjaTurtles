package com.example.ecommerce.order.service;

import com.example.ecommerce.order.DTO.ItemDTO;
import com.example.ecommerce.order.DTO.PaymentsDTO;
import com.example.ecommerce.order.DTO.RefundDTO;
import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.rabbitmq.RabbitMQProducer;
import com.example.ecommerce.order.repository.OrderRepository;
import com.example.ecommerce.order.command.*;
import com.example.ecommerce.order.rabbitmq.RabbitMQConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository repository;
    private RabbitMQProducer rabbitMQProducer;
    @Autowired
    public OrderService(OrderRepository repository,RabbitMQProducer rabbitMQProducer) {
        this.repository = repository;
        this.rabbitMQProducer = rabbitMQProducer;
    }
    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public List<Order> getOrderHistory(Long userId, Optional<String> status) {
        return status.map(s -> repository.findByUserIdAndStatus(userId, s))
                .orElseGet(() -> repository.findByUserId(userId));
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_QUEUE)
    public void receivePaymentMessage(String json) {
        try {
            System.out.println("Received payment message JSON: " + json);

            ObjectMapper mapper = new ObjectMapper();
            System.out.println("Received JSON: " + json);
            PaymentsDTO paymentdto = new ObjectMapper().readValue(json, PaymentsDTO.class);

            Double amount = paymentdto.getAmount();
            Long userId = paymentdto.getUserId();
            List<ItemDTO> items = paymentdto.getItems();

            // Create new order entity
            Order order = new Order();
            order.setUserId(userId);
            order.setTotalAmount(BigDecimal.valueOf(amount));
            order.setStatus("PLACED");  // or any initial status you want
            order.setDetails(itemsToOrderDetails(items)); // Convert ItemDTO list to your order details format

            // Place the order (uses your Command pattern)
            placeOrder(order);

            System.out.println("Order created for userId " + userId + " with amount " + amount);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // Helper method to convert List<ItemDTO> to your order details format
    private String itemsToOrderDetails(List<ItemDTO> items) {
        if (items == null || items.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (ItemDTO item : items) {
            sb.append(item.getName())
                    .append(" x")
                    .append(item.getQuantity())
                    .append(", ");
        }
        // Remove last comma and space
        if (sb.length() > 2) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    public void placeOrder(Order order) {
        OrderCommand placeCommand = new PlaceOrderCommand(order, repository);
        placeCommand.execute();
    }

    public void cancelOrder(Long orderId) {
        Optional<Order> orderOpt = repository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            Long userId= order.getUserId();
            Double amount = order.getTotalAmount().doubleValue();
            RefundDTO refundDTO = new RefundDTO(userId,amount);
            rabbitMQProducer.sendRefund(refundDTO);
        }
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
