package com.example.ecommerce.gateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Component
public class JwtExtractingGatewayFilter implements GatewayFilter, Ordered {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getURI().getPath();
    if (path.startsWith("/users/login") || path.startsWith("/users/register")) {
      return chain.filter(exchange);
    }
    // 2) Skip public CRUD: GET /users and GET/PUT/DELETE /users/{id}
    String method = exchange.getRequest().getMethod().name();
    boolean isGetAll = method.equals("GET") && path.equals("/users");
    boolean isGetById = method.equals("GET") && path.matches("^/users/\\d+$");
    boolean x = (method.equals("GET") && path.matches("^/users(/\\d+)?$"));

    boolean isUpdateById = method.equals("PUT") && path.matches("^/users/\\d+$");
    boolean isDeleteById = method.equals("DELETE") && path.matches("^/users/\\d+$");

    if (isGetAll || isGetById || isUpdateById || isDeleteById|| x) {
      return chain.filter(exchange);
    }

    String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (auth != null && auth.startsWith("Bearer ")) {
      try {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(auth.substring(7))
            .getBody();

        String userId = claims.getSubject();
        // 1) stash the attribute (if you still need it)
        exchange.getAttributes().put("userId", userId);

        // 2) mutate the request *right here* to add the header
        ServerHttpRequest mutatedReq = exchange.getRequest().mutate()
            .header("X-User-Id", userId)
            .build();

        return chain.filter(exchange.mutate().request(mutatedReq).build());
      } catch (Exception e) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
      }
    }

    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
    return exchange.getResponse().setComplete();
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }
}
