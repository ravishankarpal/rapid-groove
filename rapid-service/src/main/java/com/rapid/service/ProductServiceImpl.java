package com.rapid.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapid.core.dto.*;
import com.rapid.core.dto.product.ImagesDTO;
import com.rapid.core.dto.product.ProductDTO;
import com.rapid.core.dto.product.ReviewDTO;
import com.rapid.core.dto.product.SizeDTO;
import com.rapid.core.entity.ConfigurationKeys;
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
    private ProductRepository productRepository;

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



    @Override
    public Products addNewProduct(Products products) {
        log.info("Product: {} has been added by admin",products.getProductName());
        return productRepository.saveAndFlush(products);


    }

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

    @Override
    public byte[] getImage(String imageName) {
        imageName = imageName+".jpg";
        log.info("Image fetched successfully from Database");
        return imageModelRepository.findByName(imageName).getPicByte();
    }

    @Override
    public Page<Products> getCategoriesProducts(Integer pageNumber, String searchKey) {
        Pageable pageable = PageRequest.of(pageNumber,20);
        return productRepository.findByProductCategory(searchKey,pageable);
    }

    @Override
    public List<ProductDetailResponse> getProductDetail(boolean isSingleProductCheckOut, Integer productId) {
        List<ProductDetailResponse> products = new ArrayList<>();
        if (isSingleProductCheckOut && productId != 0){
            Optional<Products> optionalProducts = productRepository.findById(productId);
            List<ProductSizePrice> productSizePrices = productSizePriceRepository.findByProduct(productId);
            if(optionalProducts.isPresent() && !CollectionUtils.isEmpty(productSizePrices)){
                Products product = optionalProducts.get();
                ProductDetailResponse productDetailResponse = new ProductDetailResponse();
                productDetailResponse.setProductId(product.getProductId());
                productDetailResponse.setProductName(product.getProductName());
                productDetailResponse.setProductDescription(product.getProductDescription());
                productDetailResponse.setProductCategory(product.getProductCategory());
                productDetailResponse.setProductRating(product.getProductRating());
                productDetailResponse.setRatingsCount(product.getRatingsCount());
                productDetailResponse.setPromotionRequired(BooleanUtils.toBoolean(product.getIsPromotionRequired()));
                if (BooleanUtils.toBoolean(product.getIsPromotionRequired())) {
                    productDetailResponse.setPromotions(product.getPromotions());
                }
                List<ProductSizePriceResponseDTO> sizePriceResponseDTOS = new ArrayList<>();
                for(ProductSizePrice  sizePrice: productSizePrices){
                    ProductSizePriceResponseDTO productSizePriceResponseDTO = new ProductSizePriceResponseDTO();
                    productSizePriceResponseDTO.setSizePriceId(sizePrice.getSizePriceId());
                    productSizePriceResponseDTO.setSize(sizePrice.getSize());
                    productSizePriceResponseDTO.setActualPrice(sizePrice.getPrice());
                    productSizePriceResponseDTO.setDiscountPercentage(sizePrice.getDiscountPercentage());
                      productSizePriceResponseDTO.setPrice(sizePrice.getFinalPrice());
                      sizePriceResponseDTOS.add(productSizePriceResponseDTO);
                }
                productDetailResponse.setSizePrices(sizePriceResponseDTOS);
                Optional<ProductImages> productImages =  productImagesRepository.findById(productId);
                if (productImages.isPresent()){
                    Optional<ImageModel> imageModel = imageModelRepository.findById(productImages.get().getImageId());
                    if (imageModel.isPresent()){
                        Set<ImageModel> imageModelSet = new HashSet<>();
                        imageModelSet.add(imageModel.get());
                        productDetailResponse.setProductImages(imageModelSet);
                    }
                }
                products.add(productDetailResponse);
            }
        }
        return  products;
    }

    @Override
    public Products addNewProductByAdmin(ProductDetailDTO productDetailDTO, MultipartFile[] file) throws RapidGrooveException {
        Products saveProduct = null;
        try {
            String role = jwtTokenDetails.extractUserRole();
            String productDetailsJson = objectMapper.writeValueAsString(productDetailDTO);
            log.info("Admin  is going to add products {}",productDetailsJson);
            if (role.equalsIgnoreCase("Admin") && productDetailDTO != null) {
                Products products = new Products(productDetailDTO);
                Set<ImageModel> productImages = uploadImage(file);
                products.setProductImages(productImages);
                List<ProductSizePrice> sizePrices = new ArrayList<>();
                saveProduct = productRepository.saveAndFlush(products);
                for (ProductSizePriceDTO sizePriceDTO : productDetailDTO.getSizePrices()) {
                    ProductSizePrice sizePrice = new ProductSizePrice();
                    sizePrice.setSizePriceId(sizePriceDTO.getSizePriceId());
                    sizePrice.setProductId(saveProduct.getProductId());
                    sizePrice.setProductName(saveProduct.getProductName());
                    sizePrice.setPrice(sizePriceDTO.getPrice());
                    sizePrice.setSize(sizePriceDTO.getSize());
                    sizePrice.setDiscountPercentage(sizePriceDTO.getDiscountPercentage());
                    Double discountPrice = ( sizePriceDTO.getPrice() * sizePriceDTO.getDiscountPercentage() ) /100;
                    Double finalPrice = sizePriceDTO.getPrice() - discountPrice;

                    sizePrice.setFinalPrice(finalPrice);
                    Integer deliveryFeeThreshold = 0;
                    Optional<ConfigurationKeys> deliveryFeeThresholdOptional = configurationKeyRepo.findByName(Constant.DELIVERY_FEE_THRESHOLD);
                    if (deliveryFeeThresholdOptional.isPresent()){
                        deliveryFeeThreshold = Integer.parseInt( deliveryFeeThresholdOptional.get().getValue());
                    }
                    if (finalPrice > deliveryFeeThreshold){
                        Integer deliverFee  = sizePriceDTO.getDeliveryFee() != null ?sizePriceDTO.getDeliveryFee() : 0;
//                        sizePrice.setDeliveryFee(deliverFee);
                    }
                    sizePrices.add(sizePrice);
                }
                productSizePriceRepository.saveAllAndFlush(sizePrices);

            } else {
                log.info("Product details pr product image can not be blank");
            }
        } catch (Exception e){
            log.error(e.getMessage());
            throw new RapidGrooveException("There is some issue while adding new product");
        }
        return saveProduct;

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

}
