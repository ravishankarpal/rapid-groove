package com.rapid.dao;

import com.rapid.core.entity.product.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ImageModelRepository extends JpaRepository<ImageModel,Integer> {

    ImageModel findByName(String name);


}
