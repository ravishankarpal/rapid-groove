package com.rapid.service;

import com.rapid.core.entity.product.ImageModel;
import com.rapid.core.entity.product.Products;
import com.rapid.dao.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Override
    public Products addNewProduct(Products products) {
        log.info("Product: {} has been added by admin",products.getProductName());
        return productRepository.saveAndFlush(products);


    }

    @Override
    public Set<ImageModel> uploadImage(MultipartFile [] multipartFiles) throws IOException {
        Set<ImageModel> imageModels = new HashSet<>();
        for (MultipartFile file : multipartFiles){
            ImageModel imageModel = new ImageModel(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            imageModels.add(imageModel);
        }
        return imageModels;
    }

    @Override
    public List<Products> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void deleteProduct(Integer productId) {
        productRepository.deleteById(productId);
        log.info("Product has been deleted by admin {}",productId);

    }
}
