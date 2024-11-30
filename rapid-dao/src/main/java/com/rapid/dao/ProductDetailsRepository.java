package com.rapid.dao;

import com.rapid.core.entity.product.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Integer> {
    List<ProductDetails> findByNameContainingIgnoreCase(String name);

    List<ProductDetails> findByCategory(String category);
}
