package com.rapid.dao;

import com.rapid.core.entity.product.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageModelRepository extends JpaRepository<ImageModel,Integer> {

    ImageModel findByName(String name);
}
