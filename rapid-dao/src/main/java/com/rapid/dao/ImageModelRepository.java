package com.rapid.dao;

import com.rapid.core.entity.product.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface ImageModelRepository extends JpaRepository<ImageModel,Integer> {

    ImageModel findByName(String name);

    @Query(value = "select im.* from image_model im join product_images pm on im.id = pm.image_id where " +
            "im.primary_image = 1 and pm.product_id in " +
            "(select pd.id from product_details pd  where pd.sub_category =:category)", nativeQuery = true)
    Set<ImageModel> findBySubCategory(@Param("category") String category);




}
