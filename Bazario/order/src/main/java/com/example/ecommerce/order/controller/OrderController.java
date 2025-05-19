package com.example.ecommerce.order.controller;

import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.service.OrderService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

  private final OrderService service;

  public OrderController(OrderService service) {
    this.service = service;
  }

  @GetMapping
  public List<Order> getOrders(@RequestParam(required = false) String status,
      @RequestHeader(value = "X-User-Id") Long userId) {

    return service.getOrderHistory(userId, Optional.ofNullable(status));
  }

  // Get a specific order by ID
  @GetMapping("/{id}")
  public ResponseEntity<Order> getOrder(@PathVariable Long id) {
    Order order = service.getOrderById(id);
    return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
  }

  @PostMapping
  public ResponseEntity<String> placeOrder(@RequestBody Order order) {
    service.placeOrder(order);
    return ResponseEntity.ok("Order placed");
  }

  @PutMapping("/{id}/cancel")
  public ResponseEntity<String> cancelOrder(@PathVariable Long id) {
    service.cancelOrder(id);
    return ResponseEntity.ok("Order cancelled");
  }

  @PutMapping("/{id}")
  public ResponseEntity<String> updateOrder(@PathVariable Long id, @RequestBody Order order) {
    boolean updated = service.updateOrder(id, order);
    return updated ? ResponseEntity.ok("Order updated") : ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
    boolean deleted = service.deleteOrder(id);
    return deleted ? ResponseEntity.ok("Order deleted") : ResponseEntity.notFound().build();
  }

}
