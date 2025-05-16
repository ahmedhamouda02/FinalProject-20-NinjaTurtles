package com.example.ecommerce.productcart.Services;

import com.example.ecommerce.productcart.Models.Product;
import com.example.ecommerce.productcart.Strategy.FilterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductFilterContext {

    private FilterStrategy filterStrategy;



    public void setFilterStrategy(FilterStrategy filterStrategy) {
        this.filterStrategy = filterStrategy;
    }

    public List<Product> executeFilter(List<Product> products) {
        if (filterStrategy == null) {
            throw new IllegalStateException("Filter strategy not set.");
        }
        return filterStrategy.filter(products);
    }
}
