package com.rapid.web.controller;


import com.rapid.core.dto.ProductDetailResponse;
import com.rapid.core.dto.product.RateReviewRequest;
import com.rapid.core.entity.product.ProductDetails;
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

    @GetMapping(value = "/details/{productId}")
    public ResponseEntity<?> getProductDetailsByIdOrCategory(@PathVariable(name = "productId") Integer productId ) throws Exception{
        List<ProductDetails> products = productService.getProductDetailsByIdOrCategory(productId);

        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    @GetMapping(value = "/all-details")
    public ResponseEntity<?> getProductDetailsByCategory(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                         @RequestParam String searchKey ) throws Exception{
        Page<ProductDetails> products = productService.getProductDetailsByCategory(searchKey,pageNumber);

        return new ResponseEntity<>(products,HttpStatus.OK);
    }



    @GetMapping(value = "/all/details")
    public ResponseEntity<?> getAllProductDetail(@RequestParam(value = "page-number",defaultValue = "0") int pageNumber,
                                                 @RequestParam(value = "size", defaultValue = "100") int size) throws Exception{
        Page<ProductDetails> products = productService.getAllProductDetail(pageNumber,size);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }


    @GetMapping(value = "/category-details")
    public ResponseEntity<?> getProductDetailsByCategory(
            @RequestParam("category") String category) throws Exception {
        List<ProductDetails> products = productService.getProductDetailsByCategory(category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @GetMapping(value = "/search-products")
    public ResponseEntity<?> searchProductDetails(@RequestParam(value = "key", required = false)  String key,
                            @RequestParam(value = "page", required = false , defaultValue = "0") int page,
                            @RequestParam(value = "size", required = false,defaultValue = "50") int size) throws Exception {

        Page<ProductDetails> products = productService.searchProductDetails(key,page, size);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping(value = "/customer/products/rate-review")
    public ResponseEntity<?> rateAndReviewProduct(@RequestBody RateReviewRequest rateReviewRequest) throws Exception {
        String message = productService.rateAndReviewProduct(rateReviewRequest);
        return ResponseEntity.ok(message);
    }
}
