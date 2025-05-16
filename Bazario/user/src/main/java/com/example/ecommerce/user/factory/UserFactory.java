package com.example.ecommerce.user.factory;

public class UserFactory {

    public static UserType createUserType(String role) {
        return switch (role.toUpperCase()) {
            case "ADMIN" -> new Admin();
            case "BUYER" -> new Buyer();
            case "SELLER" -> new Seller();
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };
    }
}