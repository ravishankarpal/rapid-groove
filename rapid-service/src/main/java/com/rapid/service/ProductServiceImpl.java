package com.rapid.service;

import com.rapid.core.entity.order.CartItem;
import com.rapid.core.entity.product.ImageModel;
import com.rapid.core.entity.product.Products;
import com.rapid.dao.CartItemRepository;
import com.rapid.dao.ProductRepository;
import com.rapid.security.JwtRequestFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;
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
    public Page<Products> getAllProducts(Integer pageNumber, String searchKey) {
        Pageable pageable = PageRequest.of(pageNumber,20);
        if (searchKey.equals(StringUtils.EMPTY)) {
            return  productRepository.findAll(pageable);
        }else{
           return productRepository.findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(
                    searchKey,searchKey,pageable
            );
        }

    }

    @Override
    public void deleteProduct(Integer productId) {
        productRepository.deleteById(productId);
        log.info("Product has been deleted by admin {}",productId);

    }

    @Override
    public List<Products> getProductDetails(boolean isSingleProductCheckOut, Integer productId){
        if (isSingleProductCheckOut && productId != 0){
            List<Products> products = new ArrayList<>();
            Optional<Products> optionalProducts = productRepository.findById(productId);
            optionalProducts.ifPresent(products::add);
            return  products;
        }else{
            String userName = JwtRequestFilter.CURRENT_USER;
            List<CartItem> cartItems = cartItemRepository.getCartDetails(userName);
            return  cartItems.stream().map(CartItem::getProducts).collect(Collectors.toList());

        }
    }
}
