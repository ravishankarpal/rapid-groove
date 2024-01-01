package com.rapid.web.controller;


import com.rapid.core.entity.product.Products;
import com.rapid.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/details")
    public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "0") Integer pageNumber,
                                            @RequestParam(defaultValue = StringUtils.EMPTY) String searchKey){
        Page<Products> products = productService.getAllProducts(pageNumber,searchKey);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(value = "/getProductDetails/{isSingleProductCheckOut}/{productId}")
    public ResponseEntity<?> getProductDetails(@PathVariable(name = "isSingleProductCheckOut")
              boolean isSingleProductCheckOut,
             @PathVariable(name = "productId") Integer productId){
        List<Products> products = productService.getProductDetails(isSingleProductCheckOut,productId);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
}
