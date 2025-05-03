package com.example.ecommerce.user.factory;

public class Admin implements UserType {
    public String getRoleName() {
        return "ADMIN";
    }
}
