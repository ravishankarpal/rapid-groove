package com.rapid.service;

import com.rapid.core.entity.product.ImageModel;
import com.rapid.core.entity.product.Products;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ProductService {
    Products addNewProduct(Products products);

    Set<ImageModel> uploadImage(MultipartFile[] multipartFiles) throws IOException;

   List<Products> getAllProducts();

    void deleteProduct(Integer productId);
}
