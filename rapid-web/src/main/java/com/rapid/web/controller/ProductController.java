package com.rapid.web.controller;


import com.rapid.core.entity.product.Products;
import com.rapid.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/details")
    public ResponseEntity<?> getAllProducts(){
        List<Products> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }




}
