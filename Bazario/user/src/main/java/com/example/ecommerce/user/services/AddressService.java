package com.example.ecommerce.user.services;

import com.example.ecommerce.user.models.Address;
import com.example.ecommerce.user.models.User;
import com.example.ecommerce.user.repositories.AddressRepository;
import com.example.ecommerce.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

  @Autowired
  private AddressRepository addressRepository;

  @Autowired
  private UserRepository userRepository;

  // Ensure user exists
  private User getExistingUser(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " does not exist."));
  }

  // Create a new Address for the given user
  public Optional<Address> createAddress(Long userId, Address address) {
    User user = getExistingUser(userId);
    address.setUser(user);
    return Optional.of(addressRepository.save(address));
  }

  // Get one Address by its ID (no user check needed)
  public Optional<Address> getAddressById(Long userId, Long addressId) {
    User user = getExistingUser(userId);

    return addressRepository.findById(addressId);
  }

  // List all Addresses for a given user
  public List<Address> getAddressesByUserId(Long userId) {
    User user = getExistingUser(userId);
    return addressRepository.findByUser(user);
  }

  // Update an existing Address (optional: check if user still owns it)
  public Optional<Address> updateAddress(Long userId, Long addressId, Address updated) {
    User user = getExistingUser(userId);
    return addressRepository.findById(addressId)
        .map(addr -> {
          addr.setStreet(updated.getStreet());
          addr.setCity(updated.getCity());
          addr.setArea(updated.getArea());
          addr.setBuildingNumber(updated.getBuildingNumber());
          addr.setCountry(updated.getCountry());
          addr.setAddressType(updated.getAddressType());
          return addressRepository.save(addr);
        });
  }

  // Delete an Address by its ID
  public boolean deleteAddress(Long userId, Long addressId) {
    User user = getExistingUser(userId);

    if (addressRepository.existsById(addressId)) {
      addressRepository.deleteById(addressId);
      return true;
    }
    return false;
  }
}
