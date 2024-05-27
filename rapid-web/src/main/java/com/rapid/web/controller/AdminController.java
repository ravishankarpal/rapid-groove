package com.rapid.web.controller;

import com.rapid.core.entity.product.ImageModel;
import com.rapid.core.entity.product.Products;
import com.rapid.security.JwtTokenDetails;
import com.rapid.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private JwtTokenDetails jwtTokenDetails;

    @PostMapping(value = "/product/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> addNewProduct(@RequestPart("product") Products products,
                                                @RequestPart("imageFile") MultipartFile[] file) {
        try {
            String role = jwtTokenDetails.extractUserRole();
            //if (role.equalsIgnoreCase("ADMIN")) {
                Set<ImageModel> images = productService.uploadImage(file);
                products.setProductImages(images);
                Products prod = productService.addNewProduct(products);
                return new ResponseEntity<>(prod, HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>("Access Denied!", HttpStatus.UNAUTHORIZED);
//            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping(value = "/product/details")

    @DeleteMapping(value = "product/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId){

        String role = jwtTokenDetails.extractUserRole();
        if (role.equalsIgnoreCase("ADMIN")) {
            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            return new ResponseEntity<>("Access Denied!", HttpStatus.UNAUTHORIZED);
        }

    }


    @PostMapping(value = "product/upload/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile[] file) {
        try {
            productService.uploadImage(file);
            return ResponseEntity.status(HttpStatus.OK).body("Image uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
        }
    }


    @GetMapping("product/images/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {
        try {
            byte[] imageBytes  = productService.getImage(imageName);
            return new ResponseEntity<>(imageBytes, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
