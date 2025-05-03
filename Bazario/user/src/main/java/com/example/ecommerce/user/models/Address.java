package com.example.ecommerce.user.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  private String street;
  private String city;
  private String area;
  private String buildingNumber;
  private String country;
  private String addressType;

  private LocalDateTime createdAt = LocalDateTime.now();
  private LocalDateTime updatedAt = LocalDateTime.now();

  // Default constructor

  public Address() {
  }

  // Constructor with parameters
  public Address(User user, String street, String city, String area, String buildingNumber, String country,
      String addressType) {
    this.user = user;
    this.street = street;
    this.city = city;
    this.area = area;
    this.buildingNumber = buildingNumber;
    this.country = country;
    this.addressType = addressType;
  }

  // Constructor with parameters including ID

  public Address(Long id, User user, String street, String city, String area, String buildingNumber, String country,
      String addressType) {
    this.id = id;
    this.user = user;
    this.street = street;
    this.city = city;
    this.area = area;
    this.buildingNumber = buildingNumber;
    this.country = country;
    this.addressType = addressType;
  }

  // Getters and Setters

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getBuildingNumber() {
    return buildingNumber;
  }

  public void setBuildingNumber(String buildingNumber) {
    this.buildingNumber = buildingNumber;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getAddressType() {
    return addressType;
  }

  public void setAddressType(String addressType) {
    this.addressType = addressType;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

}