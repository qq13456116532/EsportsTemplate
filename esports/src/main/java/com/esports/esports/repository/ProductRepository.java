package com.esports.esports.repository;

import com.esports.esports.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Find products by category ID
    List<Product> findByCategoryId(Long categoryId);
    
    // Find products by name containing a search string
    List<Product> findByNameContainingIgnoreCase(String name);
}