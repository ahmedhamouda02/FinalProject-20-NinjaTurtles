package com.example.ecommerce.user.services;

import com.example.ecommerce.user.models.Address;
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

    // Create a new Address for the given user
    public Optional<Address> createAddress(Long userId, Address address) {
        return userRepository.findById(userId)
                .map(user -> {
                    address.setUser(user);
                    return addressRepository.save(address);
                });
    }

    // Get one Address by its ID
    public Optional<Address> getAddressById(Long addressId) {
        return addressRepository.findById(addressId);
    }

    // List all Addresses for a given user
    public List<Address> getAddressesByUserId(Long userId) {
        return userRepository.findById(userId)
                .map(addressRepository::findByUser)
                .orElse(List.of());
    }

    // Update an existing Address
    public Optional<Address> updateAddress(Long addressId, Address updated) {
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
    public boolean deleteAddress(Long addressId) {
        if (addressRepository.existsById(addressId)) {
            addressRepository.deleteById(addressId);
            return true;
        }
        return false;
    }
}
