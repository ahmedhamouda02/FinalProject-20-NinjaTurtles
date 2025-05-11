package com.example.ecommerce.productcart.Repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.ecommerce.productcart.Models.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product,String> {
}
