package com.rapid.service;

import com.rapid.core.entity.product.ImageModel;
import com.rapid.core.entity.product.Products;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ProductService {
    Products addNewProduct(Products products);

    Set<ImageModel> uploadImage(MultipartFile[] multipartFiles) throws IOException;

    Page<Products> getAllProducts(Integer pageNumber, String searchKey);

    void deleteProduct(Integer productId);

    List<Products> getProductDetails(boolean isSingleProductCheckOut, Integer productId);
}
