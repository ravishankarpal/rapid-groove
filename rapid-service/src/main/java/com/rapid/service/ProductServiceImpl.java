package com.rapid.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapid.core.dto.*;
import com.rapid.core.dto.product.*;
import com.rapid.core.entity.ConfigurationKeys;
import com.rapid.core.entity.User;
import com.rapid.core.entity.order.CartItem;
import com.rapid.core.entity.product.*;
import com.rapid.dao.*;
import com.rapid.security.JwtRequestFilter;
import com.rapid.security.JwtTokenDetails;
import com.rapid.service.exception.RapidGrooveException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{


    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    private ImageModelRepository imageModelRepository;

    @Autowired
    private ProductImagesRepository productImagesRepository;

    @Autowired
    private JwtTokenDetails jwtTokenDetails;

    @Autowired
    private ProductSizePriceRepository productSizePriceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConfigurationKeyRepo configurationKeyRepo;

   @Autowired
   private UserRepository userRepository;



    @Override
    public Set<ImageModel> uploadImage(MultipartFile [] multipartFiles) throws IOException {
        Set<ImageModel> imageModels  = new HashSet<>();
        boolean isPrimaryImage = true;
        for (MultipartFile file : multipartFiles){

            ImageModel imageModel = new ImageModel(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            imageModel.setPrimaryImage(isPrimaryImage);
            isPrimaryImage = false;
            imageModelRepository.saveAndFlush(imageModel);
            imageModels.add(imageModel);
        }
        return imageModels;

    }






    @Override
    public byte[] getImage(String imageName) {
        imageName = imageName+".jpg";
        log.info("Image fetched successfully from Database");
        return imageModelRepository.findByName(imageName).getPicByte();
    }


    @Override
    public Set<ImagesDTO> uploadProductImage(MultipartFile[] file) throws IOException {

        Set<ImagesDTO> imagesDTOS =  new HashSet<>();
        for (MultipartFile file1: file){
            ImagesDTO imagesDTO = new ImagesDTO(file1.getOriginalFilename(),
                    file1.getContentType(),
                    file1.getBytes());
            ImageModel imageModel = new ImageModel(imagesDTO);

            imageModelRepository.saveAndFlush(imageModel);
            imagesDTOS.add(imagesDTO);
        }
        return imagesDTOS;



    }

    @Override
    public ProductDetails createNewProduct(MultipartFile[] file, ProductDTO productDTO) throws IOException {
        Set<ImageModel> imageModels =  uploadImage(file);
        ProductDetails productDetails = new ProductDetails(productDTO,imageModels);
        productDetailsRepository.saveAndFlush(productDetails);
        return  productDetails;

    }

    @Override
    public List<ProductDetails> getProductDetailsByIdOrCategory(Integer productId) throws Exception {
        List<ProductDetails> productDetails = new ArrayList<>();
        ProductDetails products = productDetailsRepository.findById(productId).orElseThrow(() -> new Exception("Product not found!"));
        List<ProductDetails> detailsRepositoryByCategory = productDetailsRepository.findBySubCategory(products.getSubCategory());
        List<RelatedProduct> relatedProducts = new ArrayList<>();
        for (ProductDetails relatedProductDetails : detailsRepositoryByCategory) {
            Set<ImageModel> imageModels = imageModelRepository.findBySubCategory(products.getSubCategory());
            RelatedProduct relatedProduct = new RelatedProduct(relatedProductDetails, imageModels);
            relatedProducts.add(relatedProduct);
        }
        products.setRelatedProducts(relatedProducts);
        productDetails.add(products);
        return productDetails;

    }

    @Override
    public Page<ProductDetails> getProductDetailsByCategory(String searchKey, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber,100);
        Page<ProductDetails> productDetails = productDetailsRepository.findByNameOrCategoryContainingIgnoreCase(searchKey, pageable);

        productDetails.getContent().forEach(pd -> {
            Set<ImageModel> primaryImages = pd.getProductImages().stream()
                    .filter(pm -> pm.isPrimaryImage())
                    .collect(Collectors.toSet());
            pd.setProductImages(primaryImages);
        });
        return productDetails;
    }

    @Override
    public Page<ProductDetails> getAllProductDetail(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber,size);
        Page<ProductDetails> productDetails = productDetailsRepository.findAll(pageable);
        productDetails.getContent().forEach(pd -> {
            Set<ImageModel> primaryImages = pd.getProductImages().stream()
                    .filter(pm -> pm.isPrimaryImage())
                    .collect(Collectors.toSet());
            pd.setProductImages(primaryImages);
        });
        return productDetails;
    }

    @Override
    public List<ProductDetails> getProductDetailsByCategory(String category) {

        List<ProductDetails> productDetails = productDetailsRepository.findByCategory(category);
        productDetails.forEach(pd -> {
            Set<ImageModel> primaryImages = pd.getProductImages().stream()
                    .filter(pm -> pm.isPrimaryImage())
                    .collect(Collectors.toSet());
            pd.setProductImages(primaryImages);
        });
        return productDetails;

    }

    @Override
    public Page<ProductDetails> searchProductDetails(String key,int page, int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<ProductDetails> productDetailsPageable =productDetailsRepository
                .findByNameOrCategoryOrSubCategory(key,pageable);

        productDetailsPageable.getContent().forEach(pd -> {
            Set<ImageModel> primaryImages = pd.getProductImages().stream()
                    .filter(pm -> pm.isPrimaryImage())
                    .collect(Collectors.toSet());
            pd.setProductImages(primaryImages);
            pd.setReviews(null);
            pd.setSizes(pd.getSizes().stream()
                    .filter(size -> Boolean.TRUE.equals(size.getAvailable()))
                    .findFirst()
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList()));
            pd.setDeliveryInfo(null);

        });

        return productDetailsPageable;

    }

    @Override
    public String rateAndReviewProduct(RateReviewRequest rateReviewRequest) throws Exception {
        User user = userRepository.findById(JwtRequestFilter.CURRENT_USER).orElseThrow(()-> new Exception("User Not found!"));
        try {
            ProductDetails productDetails = productDetailsRepository.findById(rateReviewRequest.getProductId()).get();
            log.info("User {} is going to rate product {}",user.getEmail(), rateReviewRequest.getProductId() );
            ProductReview productReview = new ProductReview(rateReviewRequest,user);
            List<ProductReview> productReviews = productDetails.getReviews();
            productReviews.add(productReview);
            productDetails.setReviews(productReviews);
            int totalRating = productDetails.getReviews()
                    .stream()
                    .mapToInt(rating -> rating.getRating())
                    .filter(ratingValue -> ratingValue >= 1 && ratingValue <= 5)
                    .sum();
            long totalRatingCount = productDetails.getReviews().stream()
                    .filter(review -> review != null && review.getRating() != 0)
                    .count();
            double avgRating  =  totalRating / totalRatingCount;
            ProductRating productRating = new ProductRating();
            Integer totalReviews = productRating.getTotalReviews()!=null?productRating.getTotalReviews():0;
            productRating.setTotalRatings(totalRating);
            productRating.setAverage(avgRating);
            productRating.setTotalReviews(totalReviews+1);
            productDetails.setRating(productRating);
            productDetailsRepository.saveAndFlush(productDetails);
            log.info("User {} has successfully  rated product {}",user, rateReviewRequest.getProductId() );
            return "Review Submitted successfully!";

        }
        catch (Exception e){
            log.error("Excepetion occuured while user {} rating the product{}", user.getEmail(), rateReviewRequest.getProductId(),e);
        }
        return null;
    }

}
