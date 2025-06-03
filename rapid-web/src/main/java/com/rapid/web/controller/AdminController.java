package com.rapid.web.controller;

import com.rapid.core.dto.DeliveryAvailabilityDTO;
import com.rapid.core.dto.ProductDetailDTO;
import com.rapid.core.dto.product.ProductDTO;
import com.rapid.core.entity.DeliveryAvailability;
import com.rapid.core.entity.product.ImageModel;
import com.rapid.core.entity.product.ProductDetails;
import com.rapid.security.JwtTokenDetails;
import com.rapid.service.ProductService;
import com.rapid.service.admin.AdminService;
import com.rapid.service.exception.RapidGrooveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private JwtTokenDetails jwtTokenDetails;

    @Autowired
    private AdminService adminService;




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

    @PostMapping(value = "/update/delivery")
    public ResponseEntity<?> updateDeliveryAvailability(@RequestBody DeliveryAvailabilityDTO deliveryAvailability) throws RapidGrooveException {
        DeliveryAvailability updateDeliveryAvailability = adminService.updateDeliveryAvailability(deliveryAvailability);
        return new ResponseEntity<>(updateDeliveryAvailability, HttpStatus.OK);
    }



    @PostMapping(value = "/product/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> create(@RequestPart("product") ProductDTO productDTO,
                                                @RequestPart("file") MultipartFile[] file) throws IOException {

        //Set<ImagesDTO> imagesDTOS = productService.uploadProductImage(file);


        ProductDetails productDetails=  productService.createNewProduct(file, productDTO);
        return new ResponseEntity<>(productDetails, HttpStatus.OK);

    }
}
