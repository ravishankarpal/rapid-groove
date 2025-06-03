package com.rapid.service;


import com.rapid.core.dto.*;
import com.rapid.core.dto.cart.CartRequestDTO;
import com.rapid.core.dto.checkout.CartSummaryResponse;
import com.rapid.core.dto.checkout.CheckoutDTO;
import com.rapid.core.dto.checkout.CheckoutItemRequest;
import com.rapid.core.dto.checkout.CheckoutRequestResponse;
import com.rapid.core.entity.CheckoutItem;
import com.rapid.core.entity.CheckoutRequest;
import com.rapid.core.entity.User;
import com.rapid.core.entity.cart.CartDetails;
import com.rapid.core.entity.cart.CartItemDetails;
import com.rapid.core.entity.checkout.CheckoutDetails;
import com.rapid.core.entity.checkout.CheckoutItemEntity;
import com.rapid.core.entity.order.Cart;
import com.rapid.core.entity.order.CartItem;
import com.rapid.core.entity.product.*;
import com.rapid.dao.*;
import com.rapid.security.JwtRequestFilter;
import com.rapid.security.JwtTokenDetails;
import com.rapid.service.exception.ProductDetailsNotFoundException;
import com.rapid.service.exception.RapidGrooveException;
import com.rapid.service.exception.TokenExpiredErrorResponse;
import com.rapid.service.exception.TokenExpiredException;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CartServiceImpl extends BaseService implements CartService{



    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private CartRepository cartRepository ;

    @Autowired
    private CartItemRepository cartItemRepository ;

    @Autowired
    private JwtTokenDetails jwtTokenDetails;

    @Autowired
    private ProductSizePriceRepository productSizePriceRepository;

    @Autowired
    private CheckoutRequestRepository checkoutRequestRepository;

    @Autowired
    private CartDetailsRepository cartDetailsRepository;


    @Autowired
    private CartItemsDetailsRepository cartItemsDetailsRepository;

    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    @Autowired
    private CheckoutDetailsRepository checkoutDetailsRepository;

    @Autowired
    private ImageModelRepository imageModelRepository;




    @Override
    public List<CartItem> getCartDetails() {
        String userName = JwtRequestFilter.CURRENT_USER;
        List<CartItem> cartItems =  cartItemRepository.getCartDetails(userName);
        return  cartItems;
    }

    @Override
    public void deleteCartItem(Integer cartId) {
        cartRepository.deleteById(cartId);
        cartItemRepository.deleteById(cartId);
    }

    @Override
    public void updateCartQuantity(UpdateCartDTO updateCartDTO) throws Exception {

        String user = JwtRequestFilter.CURRENT_USER;
        if (updateCartDTO.getCartItemId() == null){
            throw new Exception("Cart Item Id can't be blank");
        }
        CartItem cartItem = cartItemRepository.findCartDetailsByUserAndCartId(user,updateCartDTO.getCartItemId());
        cartItem.setQuantity(updateCartDTO.getQuantity());
        cartItemRepository.saveAndFlush(cartItem);

    }

    @Override
    public void processCheckout(CheckoutRequestDTO checkoutRequest) throws RapidGrooveException {

        String userName = JwtRequestFilter.CURRENT_USER;
        User user = userRepository.findById(userName).orElseThrow(()-> new RapidGrooveException("User not found"));
        log.info("Start updating checkout details for user {}:", user.getEmail());
        checkoutRequestRepository.deleteCheckoutItemsByUserId(user.getEmail());
        checkoutRequestRepository.deleteCheckoutRequestsByUserId(user.getEmail());
        CheckoutRequest checkout = new CheckoutRequest();
        List<CheckoutItem> checkoutItems = new ArrayList<>();
        for(CheckoutItemDTO checkoutItemDTO :checkoutRequest.getCheckoutItems()){
            CheckoutItem checkoutItem = new CheckoutItem(checkoutItemDTO);
            checkoutItems.add(checkoutItem);
        }
        checkout.setCheckoutItems(checkoutItems);
        checkout.setUser(user);
        checkout.setTotalAmount(checkoutRequest.getTotalAmount());
        checkout.setDiscountAmount(checkoutRequest.getDiscountAmount());
        checkout.setDeliveryFee(checkoutRequest.getDeliveryFee());
        checkoutRequestRepository.saveAndFlush(checkout);
        log.info("Successfully checkout by for user {}:", user.getEmail());



    }

    @Override
    public CheckoutResponse getCheckoutDetails() throws RapidGrooveException {
        String currentUser = JwtRequestFilter.CURRENT_USER;
        User user = userRepository.findById(currentUser).orElseThrow(()-> new RapidGrooveException("User not found!"));
        Object[] objects =  checkoutRequestRepository.findByUserId(user.getEmail());
        CheckoutResponse  checkoutResponse = new CheckoutResponse();
        if(objects == null ||objects.length==0 ) {
            log.info("No details found");
        }
        List<CheckoutItemResponse> checkoutItemResponses = new ArrayList<>();
        boolean isTotalAmountAdded = false;
        boolean isDiscountAmountAdded = false;
        boolean isDeliverFeeIncluded = false;
        for (Object obj : objects) {
            if (obj instanceof Object[]) {
                Object[] row = (Object[]) obj;
                CheckoutItemResponse checkoutItemResponse = new CheckoutItemResponse(); // Create a new CheckoutResponse instance
                checkoutItemResponse.setProductId((Integer) row[0]);          // product_id
                checkoutItemResponse.setProductName((String) row[1]);         // product_name
                checkoutItemResponse.setQuantity((Integer) row[2]);           // quantity
                checkoutItemResponse.setSize((String) row[3]);                // size
                checkoutItemResponse.setPrice((BigDecimal) row[4]);           // price
                checkoutItemResponse.setPicByte((byte[]) row[5]);             // pic_byte
                checkoutItemResponses.add(checkoutItemResponse);
                if (!isTotalAmountAdded){
                    checkoutResponse.setTotalAmount((BigDecimal) row[6]);
                    isTotalAmountAdded = true;
                }

                if (!isDiscountAmountAdded){
                    checkoutResponse.setDiscountAmount((Double) row[7]);
                    isDiscountAmountAdded = true;
                }

                if (!isDeliverFeeIncluded){
                    checkoutResponse.setDeliveryFee((String) row[8]);
                    isDeliverFeeIncluded = true;
                }
                checkoutResponse.setCheckoutItemResponses(checkoutItemResponses);

            }
        }

        return checkoutResponse;

    }

    @Override
    public void addItemToCartV2(CartRequestDTO cartRequestDTO) throws Exception {
        String userName = JwtRequestFilter.CURRENT_USER;
        User user  = userRepository.findById(userName).orElseThrow( () -> new Exception("User Not Found"));
        Integer productId = cartRequestDTO.getProductId();
        log.info("going to add product in cart for user:{} for productId :{}",userName,productId);
        CartDetails cartDetails = cartDetailsRepository.findByUser(user);
        if (cartDetails == null){
            cartDetails = new CartDetails(user);
        }
        ProductDetails product = productDetailsRepository.findById(productId)
                .orElseThrow(() -> new Exception("Product not found"));



        ProductSize selectedSize = product.getSizes().stream()
                .filter(size -> size.getValue().equalsIgnoreCase(cartRequestDTO.getSelectedSize()))
                .filter(size -> size.getAvailable() != null && size.getAvailable()) // Check if size is available
                .findFirst()
                .orElseThrow(() -> {
                    boolean sizeExists = product.getSizes().stream()
                            .anyMatch(size -> size.getValue().equals(cartRequestDTO.getSelectedSize()));
                    if (!sizeExists) {
                        throw new IllegalArgumentException("Selected size does not exist for this product");
                    } else {
                        throw new IllegalArgumentException("Selected size is currently unavailable");
                    }
                });

        if (selectedSize.getPrice() == null) {
            throw new IllegalStateException("No pricing information available for the selected size");
        }

        int totalExistingQuantity = cartDetails.getCartItemDetails().stream()
                .filter(item ->
                        item.getProduct().getId().equals(product.getId()) &&
                                item.getSelectedSize().getValue().equals(selectedSize.getValue()))
                .mapToInt(CartItemDetails::getQuantity)
                .sum();

        if (totalExistingQuantity > 0) {
            cartDetails.getCartItemDetails().stream()
                    .filter(item ->
                            item.getProduct().getId().equals(product.getId()) &&
                                    item.getSelectedSize().getValue().equals(selectedSize.getValue()))
                    .forEach(item -> item.setQuantity(item.getQuantity() + cartRequestDTO.getQuantity()));
        } else {
            // Create new cart item if no matching items exist
            CartItemDetails newCartItem = new CartItemDetails();
            newCartItem.setProduct(product);
            newCartItem.setSelectedSize(selectedSize);
            newCartItem.setQuantity(cartRequestDTO.getQuantity());
            cartDetails.getCartItemDetails().add(newCartItem);
        }
        if (cartDetails.getUser() == null) {
            cartDetails.setUser(user);
        }
        cartDetailsRepository.saveAndFlush(cartDetails);
        log.info("Item added in cart successfully for user {}", userName);

    }

    @Override
    public CartDetails getItem() throws TokenExpiredException,Exception {
        String userName = JwtRequestFilter.CURRENT_USER;
        checkTokenExpiration();

        User user  = userRepository.findById(userName).orElseThrow( () -> new Exception("User Not Found"));

       CartDetails cartDetails = cartDetailsRepository.findByUser(user);
        if (cartDetails == null) {
            cartDetails = new CartDetails(user);
        }
        cartDetails.getCartItemDetails().forEach(cartItem -> {
            Set<ImageModel> primaryImages = cartItem.getProduct().getProductImages().stream()
                    .filter(ImageModel::isPrimaryImage)
                    .collect(Collectors.toSet());
            cartItem.getProduct().setProductImages(primaryImages);
            cartItem.getProduct().setRating(null);
            cartItem.getProduct().setReviews(null);
            ProductDetails product = cartItem.getProduct();
            ProductSize selectedSize = cartItem.getSelectedSize();

            List<ProductSize> filteredSizes = product.getSizes().stream()
                    .filter(size -> size.getValue().equals(selectedSize.getValue()))
                    .collect(Collectors.toList());
            product.setSizes(filteredSizes);
        });

        cartDetails.setUser(null);
        return cartDetails;

    }

    @Override
    public void delete(Long cartId) throws Exception {
        String userName = JwtRequestFilter.CURRENT_USER;
        log.info("Deleting cart for user {}", userName);
        CartItemDetails cartItemDetails= cartItemsDetailsRepository.findByUserIdAndCartItem(userName, cartId)
                .orElseThrow(() -> new Exception("Cart item not found"));
        CartDetails cartDetails = cartDetailsRepository.findByCartItemDetailsContaining(cartItemDetails)
                .orElseThrow(() -> new Exception("Cart item not found"));
        cartDetails.getCartItemDetails().remove(cartItemDetails);
        cartDetailsRepository.saveAndFlush(cartDetails);




    }

    @Override
    public CheckoutRequestResponse saveCheckoutDetails(com.rapid.core.dto.checkout.CheckoutRequest checkoutRequest) {
        String username = JwtRequestFilter.CURRENT_USER;
        User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("User Not found!"));
        log.info("Start saving checkout details for product id {} for user {}",username );
        List<CheckoutDetails> details = checkoutDetailsRepository.findByUser(username);
        if(!CollectionUtils.isEmpty(details)){
            checkoutDetailsRepository.deleteByUser(username);
        }
        CheckoutDetails checkoutDetails = new CheckoutDetails(checkoutRequest, user);
        checkoutDetailsRepository.saveAndFlush(checkoutDetails);
        CheckoutRequestResponse checkoutRequestResponse = new CheckoutRequestResponse();
        List<Integer> productId = new ArrayList<>();
        productId.addAll(
                checkoutDetails.getItemRequests()
                        .stream()
                        .filter(id -> id.getProductId() != null)
                        .map(id -> id.getProductId())
                        .collect(Collectors.toList())
        );
         List<String> productSize = new ArrayList<>();
        productSize.addAll(checkoutDetails.getItemRequests()
                 .stream()
                 .filter(size -> size.getSize() != null)
                 .map(size -> size.getSize())
                 .collect(Collectors.toList()));

        checkoutRequestResponse.setProductId(productId);
        checkoutRequestResponse.setProductSize(productSize);

        log.info("Successfully saved checkout details for product id {} for user {}",username );
        return checkoutRequestResponse;
    }

//    @Override
//    public com.rapid.core.dto.checkout.CheckoutResponse getCheckoutDetails(CheckoutDTO checkoutDTO) throws Exception {
//        String username = JwtRequestFilter.CURRENT_USER;
//        User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("User Not found!"));
//        log.info("Start fetching checkout details for product id {} for user {}",username );
//        CheckoutDetails checkoutDetails = checkoutDetailsRepository.findByProductIdAndSizeAndUser(checkoutDTO.getProductId(),checkoutDTO.getProductSize(),user.getEmail());
//        List<com.rapid.core.dto.checkout.CheckoutItemResponse> itemResponses = new ArrayList<>();
//        for (CheckoutItemEntity checkoutItemEntity : checkoutDetails.getItemRequests()){
//            byte image[] = imageModelRepository.findImageByProductId(checkoutItemEntity.getProductId());
//            ProductDetails productDetails = productDetailsRepository.findById(checkoutItemEntity.getProductId()).orElseThrow(() -> new Exception("product Not found!"));
//            com.rapid.core.dto.checkout.CheckoutItemResponse checkoutItemResponse = new com.rapid.core.dto.checkout.CheckoutItemResponse(checkoutItemEntity, image, productDetails);
//            itemResponses.add(checkoutItemResponse);
//        }
//
//
//
//        CartSummaryResponse cartSummaryResponse = new CartSummaryResponse(checkoutDetails);
//        com.rapid.core.dto.checkout.CheckoutResponse checkoutResponse = new com.rapid.core.dto.checkout.CheckoutResponse();
//        checkoutResponse.setItemResponses(itemResponses);
//        checkoutResponse.setCartSummaryResponse(cartSummaryResponse);
//        log.info("Successfully fetched checkout details for product id {} for user {}",username );
//        return checkoutResponse;
//
//    }

    @Override
    public com.rapid.core.dto.checkout.CheckoutResponse getCheckoutDetails(CheckoutDTO checkoutDTO) throws Exception {
        String username = JwtRequestFilter.CURRENT_USER;
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not found!"));
        log.info("Start fetching checkout details for user {}", username);

        // Validate input
        if (checkoutDTO.getProductId().size() != checkoutDTO.getProductSize().size()) {
            throw new IllegalArgumentException("Mismatch between product IDs and sizes.");
        }

        List<com.rapid.core.dto.checkout.CheckoutItemResponse> itemResponses = new ArrayList<>();
        CheckoutDetails checkoutDetails  = null;

        for (int i = 0; i < checkoutDTO.getProductId().size(); i++) {
            Integer productId = checkoutDTO.getProductId().get(i);
            String size = checkoutDTO.getProductSize().get(i);

            // Fetch checkout details for each product and size
             checkoutDetails = checkoutDetailsRepository.findByProductIdAndSizeAndUser(
                    productId, size, user.getEmail());

            if (checkoutDetails == null) {
                log.warn("No checkout details found for product ID {} and size {}", productId, size);
                continue; // Skip if no details found
            }

//            for (CheckoutItemEntity checkoutItemEntity : checkoutDetails.getItemRequests()) {
//                ProductDetails productDetails = productDetailsRepository.findById(checkoutItemEntity.getProductId())
//                        .orElseThrow(() -> new Exception("Product not found for ID: " + checkoutItemEntity.getProductId()));
//                com.rapid.core.dto.checkout.CheckoutItemResponse checkoutItemResponse = new com.rapid.core.dto.checkout.CheckoutItemResponse(
//                        checkoutItemEntity, productDetails);
//                itemResponses.add(checkoutItemResponse);
//            }
        }

        for (CheckoutItemEntity checkoutItemEntity : checkoutDetails.getItemRequests()) {
            ProductDetails productDetails = productDetailsRepository.findById(checkoutItemEntity.getProductId())
                    .orElseThrow(() -> new Exception("Product not found for ID: " + checkoutItemEntity.getProductId()));
            com.rapid.core.dto.checkout.CheckoutItemResponse checkoutItemResponse = new com.rapid.core.dto.checkout.CheckoutItemResponse(
                    checkoutItemEntity, productDetails);
            itemResponses.add(checkoutItemResponse);
        }

        // Create a summary response (assumes only one cart summary; adjust logic if needed)
        CartSummaryResponse cartSummaryResponse = new CartSummaryResponse();
        if (!itemResponses.isEmpty()) {
            CheckoutDetails details = checkoutDetailsRepository.findByProductIdAndSizeAndUser(
                    checkoutDTO.getProductId().get(0), checkoutDTO.getProductSize().get(0), user.getEmail());
            cartSummaryResponse = new CartSummaryResponse(details);
        }

        // Build the response
        com.rapid.core.dto.checkout.CheckoutResponse checkoutResponse = new com.rapid.core.dto.checkout.CheckoutResponse();
        checkoutResponse.setItemResponses(itemResponses);
        checkoutResponse.setCartSummaryResponse(cartSummaryResponse);

        log.info("Successfully fetched checkout details for user {}", username);
        return checkoutResponse;
    }


//
//    @Override
//    public com.rapid.core.dto.checkout.CheckoutResponse getCheckoutDetails(CheckoutDTO checkoutDTO) throws Exception {
//        String username = JwtRequestFilter.CURRENT_USER;
//        User user = userRepository.findById(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User Not found!"));
//        log.info("Start fetching checkout details for user {}", username);
//
//        // Validate input
//        if (checkoutDTO.getProductId().size() != checkoutDTO.getProductSize().size()) {
//            throw new IllegalArgumentException("Mismatch between product IDs and sizes.");
//        }
//
//        List<com.rapid.core.dto.checkout.CheckoutItemResponse> itemResponses = new ArrayList<>();
//        Set<String> processedProducts = new HashSet<>(); // To track processed productId + size combinations
//
//        for (int i = 0; i < checkoutDTO.getProductId().size(); i++) {
//            Integer productId = checkoutDTO.getProductId().get(i);
//            String size = checkoutDTO.getProductSize().get(i);
//
//            String productKey = productId + "_" + size; // A unique key for each product and size
//
//            if (processedProducts.contains(productKey)) {
//                continue; // Skip if this product and size have already been processed
//            }
//
//            // Mark this product and size as processed
//            processedProducts.add(productKey);
//
//            // Fetch checkout details for each product and size
//            CheckoutDetails checkoutDetails = checkoutDetailsRepository.findByProductIdAndSizeAndUser(
//                    productId, size, user.getEmail());
//
//            if (checkoutDetails == null) {
//                log.warn("No checkout details found for product ID {} and size {}", productId, size);
//                continue; // Skip if no details found
//            }
//
//            for (CheckoutItemEntity checkoutItemEntity : checkoutDetails.getItemRequests()) {
//                ProductDetails productDetails = productDetailsRepository.findById(checkoutItemEntity.getProductId())
//                        .orElseThrow(() -> new Exception("Product not found for ID: " + checkoutItemEntity.getProductId()));
//                com.rapid.core.dto.checkout.CheckoutItemResponse checkoutItemResponse = new com.rapid.core.dto.checkout.CheckoutItemResponse(
//                        checkoutItemEntity, productDetails);
//                itemResponses.add(checkoutItemResponse);
//            }
//        }
//
//        // Create a summary response (assumes only one cart summary; adjust logic if needed)
//        CartSummaryResponse cartSummaryResponse = new CartSummaryResponse();
//        if (!itemResponses.isEmpty()) {
//            CheckoutDetails checkoutDetails = checkoutDetailsRepository.findByProductIdAndSizeAndUser(
//                    checkoutDTO.getProductId().get(0), checkoutDTO.getProductSize().get(0), user.getEmail());
//            cartSummaryResponse = new CartSummaryResponse(checkoutDetails);
//        }
//
//        // Build the response
//        com.rapid.core.dto.checkout.CheckoutResponse checkoutResponse = new com.rapid.core.dto.checkout.CheckoutResponse();
//        checkoutResponse.setItemResponses(itemResponses);
//        checkoutResponse.setCartSummaryResponse(cartSummaryResponse);
//
//        log.info("Successfully fetched checkout details for user {}", username);
//        return checkoutResponse;
//    }


}
