package com.example.Paymentgp.model;

public enum DiscountCode {
    NO_DISCOUNT(0),
    DISCOUNT_10(0.10),
    DISCOUNT_20(0.20);

    private final double discountAmount;

    DiscountCode(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public static DiscountCode fromString(String code) {
        for (DiscountCode discountCode : DiscountCode.values()) {
            if (discountCode.name().equalsIgnoreCase(code)) {
                return discountCode;
            }
        }
        return NO_DISCOUNT; // Default to no discount if code is invalid
    }
}
