package com.example.ecommerce.user.controllers;

import com.example.ecommerce.user.models.Address;
import com.example.ecommerce.user.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users/{userId}/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // CREATE
    @PostMapping
    public ResponseEntity<Address> createAddress(
            @PathVariable Long userId,
            @RequestBody Address address
    ) {
        return addressService.createAddress(userId, address)
                .map(saved -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(saved))
                .orElse(ResponseEntity.notFound().build());
    }

    // READ ALL for a user
    @GetMapping
    public ResponseEntity<List<Address>> listAddresses(@PathVariable Long userId) {
        List<Address> addrs = addressService.getAddressesByUserId(userId);
        return ResponseEntity.ok(addrs);
    }

    // READ ONE
    @GetMapping("/{addressId}")
    public ResponseEntity<Address> getAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId
    ) {
        Optional<Address> addr = addressService.getAddressById(addressId);
        return addr.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{addressId}")
    public ResponseEntity<Address> updateAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId,
            @RequestBody Address updated
    ) {
        return addressService.updateAddress(addressId, updated)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId
    ) {
        boolean deleted = addressService.deleteAddress(addressId);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
