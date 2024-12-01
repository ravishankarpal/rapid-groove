package com.rapid.web.controller;


import com.rapid.core.dto.ProductDetailResponse;
import com.rapid.core.entity.product.ProductDetails;
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

    @GetMapping(value = "/details/categories")
    public ResponseEntity<?> getCategoriesProducts(@RequestParam(defaultValue = "0") Integer pageNumber,
                                            @RequestParam(defaultValue = StringUtils.EMPTY) String searchKey){
        Page<Products> products = productService.getCategoriesProducts(pageNumber,searchKey);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(value = "/getProductDetails/{isSingleProductCheckOut}/{productId}")
    public ResponseEntity<?> getProductDetails(@PathVariable(name = "isSingleProductCheckOut")
              boolean isSingleProductCheckOut,
             @PathVariable(name = "productId") Integer productId){
        List<Products> products = productService.getProductDetails(isSingleProductCheckOut,productId);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }


    @GetMapping(value = "/productDetails/{isSingleProductCheckOut}/{productId}")
    public ResponseEntity<?> getProductDetail(@PathVariable(name = "isSingleProductCheckOut")
                                               boolean isSingleProductCheckOut,
                                               @PathVariable(name = "productId") Integer productId){
        List<ProductDetailResponse> products = productService.getProductDetail(isSingleProductCheckOut,productId);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }



//    @PostMapping
//    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
//        ProductDTO createdProduct = productDetailsService.createProduct(productDTO);
//        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
//    }
//
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer id) {
//        ProductDTO product = productDetailsService.getProductById(id);
//        return new ResponseEntity<>(product, HttpStatus.OK);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<ProductDTO>> getAllProducts() {
//        List<ProductDTO> products = productDetailsService.getAllProducts();
//        return ResponseEntity.ok(products);
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String keyword) {
//        List<ProductDTO> products = productDetailsService.searchProducts(keyword);
//        return ResponseEntity.ok(products);
//    }



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
    public ResponseEntity<?> getAllProductDetail(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                 @RequestParam("category") String category,
                                                 @RequestParam("sub_category") String subCategory) throws Exception{
        Page<ProductDetails> products = productService.getAllProductDetail(pageNumber);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }


    @GetMapping(value = "/category-details")
    public ResponseEntity<?> getProductDetailsByCategory(
            @RequestParam("category") String category) throws Exception {
        List<ProductDetails> products = productService.getProductDetailsByCategory(category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
