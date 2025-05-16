package com.example.ecommerce.user.controllers;

import com.example.ecommerce.user.models.Address;
import com.example.ecommerce.user.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // CREATE
    @PostMapping
    public ResponseEntity<?> createAddress(
            @PathVariable Long userId,
            @RequestBody Address address
    ) {
        Optional<Address> created = addressService.createAddress(userId, address);
        if (created.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(created.get());
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User with ID " + userId + " not found");
        }
    }

    // READ ALL for a user
    @GetMapping
    public ResponseEntity<?> listAddresses(@PathVariable Long userId) {
        List<Address> addrs = addressService.getAddressesByUserId(userId);
        if (addrs.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No addresses found for user ID " + userId);
        }
        return ResponseEntity.ok(addrs);
    }

    // READ ONE
    @GetMapping("/{addressId}")
    public ResponseEntity<?> getAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId
    ) {
        Optional<Address> addr = addressService.getAddressById(addressId);
        if (addr.isPresent()) {
            return ResponseEntity.ok(addr.get());
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Address with ID " + addressId + " not found");
        }
    }

    // UPDATE
    @PutMapping("/{addressId}")
    public ResponseEntity<?> updateAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId,
            @RequestBody Address updated
    ) {
        Optional<Address> upd = addressService.updateAddress(addressId, updated);
        if (upd.isPresent()) {
            return ResponseEntity.ok(upd.get());
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Cannot update — address ID " + addressId + " not found");
        }
    }

    // DELETE
    @DeleteMapping("/{addressId}")
    public ResponseEntity<?> deleteAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId
    ) {
        boolean deleted = addressService.deleteAddress(addressId);
        if (deleted) {
            return ResponseEntity.ok("Address with ID " + addressId + " deleted successfully");
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Address with ID " + addressId + " not found");
        }
    }
}
