package com.example.ecommerce.productcart.Feign;

import com.example.ecommerce.productcart.DTO.PaymentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentClient {
    @PostMapping("/api/payments")
    String makePayment(@RequestBody PaymentDTO payment);
}