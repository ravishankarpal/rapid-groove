package com.rapid.service;

import com.rapid.core.dto.ProductDetailDTO;
import com.rapid.core.dto.ProductDetailResponse;
import com.rapid.core.dto.product.ImagesDTO;
import com.rapid.core.dto.product.ProductDTO;
import com.rapid.core.dto.product.RateReviewRequest;
import com.rapid.core.entity.product.ImageModel;
import com.rapid.core.entity.product.ProductDetails;
import com.rapid.service.exception.RapidGrooveException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ProductService {

    Set<ImageModel> uploadImage(MultipartFile[] multipartFiles) throws IOException;




    byte[] getImage(String imageName);





    Set<ImagesDTO> uploadProductImage(MultipartFile[] file) throws IOException;

    ProductDetails createNewProduct(MultipartFile[] file, ProductDTO productDTO) throws IOException;

    List<ProductDetails> getProductDetailsByIdOrCategory(Integer productId) throws Exception;

    Page<ProductDetails> getProductDetailsByCategory(String searchKey, Integer pageNumber);

    Page<ProductDetails> getAllProductDetail(int pageNumber, int size);

    List<ProductDetails> getProductDetailsByCategory(String category);

    Page<ProductDetails> searchProductDetails(String key, int page, int size);

    String rateAndReviewProduct(RateReviewRequest rateReviewRequest) throws Exception;
}
