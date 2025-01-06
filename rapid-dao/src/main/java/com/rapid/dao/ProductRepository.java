package com.rapid.dao;

import com.rapid.core.entity.product.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {

    Page<Products> findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(String key1, String key2, Pageable pageable);

    Page<Products> findByProductCategory(String category, Pageable pageable);



}
