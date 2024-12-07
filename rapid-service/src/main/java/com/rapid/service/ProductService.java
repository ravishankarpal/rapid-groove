package com.rapid.service;

import com.rapid.core.dto.ProductDetailDTO;
import com.rapid.core.dto.ProductDetailResponse;
import com.rapid.core.entity.product.ImageModel;
import com.rapid.core.entity.product.Products;
import com.rapid.service.exception.RapidGrooveException;
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

    byte[] getImage(String imageName);

    Page<Products> getCategoriesProducts(Integer pageNumber, String searchKey);

    List<ProductDetailResponse> getProductDetail(boolean isSingleProductCheckOut, Integer productId);

    Products addNewProductByAdmin(ProductDetailDTO productDetailDTO, MultipartFile[] file) throws RapidGrooveException;
}
