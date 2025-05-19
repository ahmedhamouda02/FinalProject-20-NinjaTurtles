package com.example.ecommerce.productcart.Feign;

import com.example.ecommerce.productcart.DTO.PaymentDTO;
import com.example.ecommerce.productcart.DTO.PaymentsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "payment-service", url = "http://host.docker.internal:8083")
public interface PaymentClient {

    @PostMapping("/payments")
    String makePayment(@RequestBody PaymentsDTO payment);

//     @PostMapping("/payments")
//     String makePayment(@RequestBody PaymentDTO payment);

}