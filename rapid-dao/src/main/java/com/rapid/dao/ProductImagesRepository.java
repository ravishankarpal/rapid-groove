package com.rapid.dao;

import com.rapid.core.entity.product.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImagesRepository extends JpaRepository<ProductImages, Integer> {
}
