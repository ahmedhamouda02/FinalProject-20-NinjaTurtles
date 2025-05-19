package com.example.ecommerce.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

  @Autowired
  private JwtExtractingGatewayFilter jwtFilter;

  @Bean
  public RouteLocator customRoutes(RouteLocatorBuilder builder) {
    return builder.routes()
        .route("user-service", r -> r
            .path("/users/**", "/user-cart/**")
            .filters(f -> f.filter(jwtFilter))
            .uri("http://localhost:8081"))

        // 2) New: Cart service (also secured by JWT)
        .route("cart-service", r -> r
            .path("/carts/**")
            .filters(f -> f.filter(jwtFilter))
            .uri("http://localhost:8082"))

        .build();
  }
}
