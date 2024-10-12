package com.rapid.dao;

import com.rapid.core.entity.product.ProductSizePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ProductSizePriceRepository extends JpaRepository<ProductSizePrice, Integer> {

    @Query(value = "select * from product_size_prices where product_id =:productId",nativeQuery = true)
    List<ProductSizePrice> findByProduct(Integer productId);
}
