package com.example.ecommerce.user.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  private final long EXPIRATION = 1000 * 60 * 60; // 1 hour

  public String generateToken(Long userId) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

    return Jwts.builder()
        .setSubject(String.valueOf(userId))
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Keys.hmacShaKeyFor(secret.getBytes()); // parse key
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Long extractUserId(String token) {
    return Long.parseLong(
        Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject());
  }
}
