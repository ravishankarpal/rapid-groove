package com.rapid.dao;

import com.rapid.core.entity.product.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface ImageModelRepository extends JpaRepository<ImageModel,Integer> {

    ImageModel findByName(String name);

    @Query(value = "select im.* from image_model im join product_images pm on im.id = pm.image_id where " +
            "pm.product_id =:productId and im.primary_image = 0", nativeQuery = true)
    Set<ImageModel> findByProductId(@Param("productId") Integer productId);




}
