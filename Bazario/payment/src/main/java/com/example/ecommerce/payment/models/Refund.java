package com.example.ecommerce.payment.models;

import jakarta.persistence.*;

@Entity
@Table(name = "refunds")
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer points;

    private Long userId;  // add this

    public Refund() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
