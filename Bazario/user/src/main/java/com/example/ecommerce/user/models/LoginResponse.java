package com.example.ecommerce.user.models;

public class LoginResponse {
  private Long id;
  private String token;

  public LoginResponse(Long id, String token) {
    this.id = id;
    this.token = token;
  }

  public Long getId() {
    return id;
  }

  public String getToken() {
    return token;
  }
}
