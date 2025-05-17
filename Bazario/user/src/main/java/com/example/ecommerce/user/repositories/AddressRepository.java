package com.example.ecommerce.user.repositories;

import com.example.ecommerce.user.models.Address;
import com.example.ecommerce.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
}