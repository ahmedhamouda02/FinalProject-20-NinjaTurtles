package com.example.ecommerce.user.models;

public class LoginRequest {
    private String email;
    private String password;

    // Required: No-arg constructor
    public LoginRequest() {}

    // Optional: All-args constructor
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // ✅ Getters & Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
