package com.rapid.dao;

import com.rapid.core.entity.product.ProductDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Integer> {

    List<ProductDetails> findByCategory(String category);

//    @Query(value = "SELECT * FROM product_details WHERE name =:searchKey or category =:searchKey", nativeQuery = true)
//    List<ProductDetails> findByNameOrCategoryContainingIgnoreCase(@Param("searchKey") String searchKey, Pageable pageable);

    @Query(value = "SELECT * FROM product_details WHERE LOWER(name) = LOWER(:searchKey) OR LOWER(category) = LOWER(:searchKey)", nativeQuery = true)
    Page<ProductDetails> findByNameOrCategoryContainingIgnoreCase(@Param("searchKey") String searchKey, Pageable pageable);

//    @Query(value = "select pd.* from product_details pd join product_images pi on pd.id = pi.product_id " +
//            "join image_model im on im.id = pi.image_id where lower(pd.name) = lower(:searchKey) or " +
//            "lower(category) = lower(:searchKey) and im.primary_image = 1", nativeQuery = true)
//    Page<ProductDetails> findByNameOrCategoryContainingIgnoreCase(@Param("searchKey") String searchKey, Pageable pageable);

    List<ProductDetails> findBySubCategory(String category);


}
