package com.rapid.service;


import com.rapid.core.dto.*;
import com.rapid.core.dto.cart.CartRequestDTO;
import com.rapid.core.entity.CheckoutItem;
import com.rapid.core.entity.CheckoutRequest;
import com.rapid.core.entity.User;
import com.rapid.core.entity.cart.CartDetails;
import com.rapid.core.entity.cart.CartItemDetails;
import com.rapid.core.entity.order.Cart;
import com.rapid.core.entity.order.CartItem;
import com.rapid.core.entity.product.*;
import com.rapid.dao.*;
import com.rapid.security.JwtRequestFilter;
import com.rapid.security.JwtTokenDetails;
import com.rapid.service.exception.ProductDetailsNotFoundException;
import com.rapid.service.exception.RapidGrooveException;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CartServiceImpl implements CartService{

    @Autowired
    private ProductRepository productRepository;

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
    @Override
        public void addToCart(Integer productId) {
            log.info("going to add product in cart for productId :{}",productId);
            Optional<Products> products = productRepository.findById(productId);
            String userName  = null;
            if (products.isPresent()){
                userName =  JwtRequestFilter.CURRENT_USER;
                Optional<User> user = userRepository.findById(userName);
                if (user.isPresent()){
                    log.info("going to add product in cart for productId :{} by user :{}"
                            ,productId,user.get().getEmail());
                    List<Cart> cart = cartRepository.findCartByUserId(user.get().getEmail());
                    if(CollectionUtils.isEmpty(cart)){
                        cart.add(new Cart(user.get()));
                    }
                    List<CartItem> existingItem = cart.get(0).getCartItems().stream()
                            .filter(item -> item.getProducts().equals(products.get()))
                            .toList();

                    if (!CollectionUtils.isEmpty(existingItem)) {
                        existingItem.get(0).setQuantity(existingItem.get(0).getQuantity() + 1);
                    } else {
                        CartItem cartItem = new CartItem();
                        cartItem.setProducts(products.get());
                        cartItem.setQuantity(1);
                        cartItem.setCart(cart.get(0));
                        cart.get(0).getCartItems().add(cartItem);
                    }
                    cartRepository.saveAndFlush(cart.get(0));
                    log.info("Product has been successfully added in user cart username :{} cartDetails :{}",
                            userName,cart);
                }
            }
            else{
                log.info("Product Details not found!");
                throw  new ProductDetailsNotFoundException("Product Details not found!");
            }
        }









    @Override
    public void addItemToCart(AddToCartRequestDTO requestDTO) throws RapidGrooveException {
        Integer productId = requestDTO.getProductId();
        log.info("going to add product in cart for productId :{}",productId);


        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new RapidGrooveException("Product not found"));

        ProductSizePrice sizePrice = productSizePriceRepository.findById(requestDTO.getSizePriceId())
                .orElseThrow(() -> new RapidGrooveException("Product size not found"));

        String userName =  JwtRequestFilter.CURRENT_USER;
        User user = userRepository.findById(userName).orElseThrow(() -> new RapidGrooveException("User not found"));
        Cart cart = cartRepository.findCartByUserName(userName);
        if (cart == null){
            cart = new Cart(user);
        }

//        CartItem existingCartItem = cart.getCartItems().stream().filter(item -> item.getProducts().getProductId().equals(product.getProductId())
//        && item.getProductSizePrice().getSizePriceId().equals(sizePrice.getSizePriceId())).findFirst().orElse(null);

        List<CartItem> items = Optional.ofNullable(cart.getCartItems()).orElse(new ArrayList<>());
        CartItem existingCartItem = items.stream()
                .filter(item -> item.getProducts().getProductId().equals(product.getProductId())
                        && item.getProductSizePrice().getSizePriceId().equals(sizePrice.getSizePriceId()))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + requestDTO.getQuantity());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProducts(product);
            cartItem.setQuantity(requestDTO.getQuantity());
            cartItem.setPrice(sizePrice.getFinalPrice());
            cartItem.setCart(cart);
            if (cart.getCartItems() == null) {
                cart.setCartItems(new ArrayList<>());
            }
            cartItem.setProductSizePrice(sizePrice);
            cart.getCartItems().add(cartItem);
        }
        cartRepository.saveAndFlush(cart);
        log.info("Item added in cart successfully for user {}", userName);

    }



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


//    @Override
//    public List<CartItemResponseDTO> getCartCartDetailsFomUserToken() {
//        List<CartItemResponseDTO> cartItemResponseDTOS = new ArrayList<>();
//        CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO();
//        String userName = JwtRequestFilter.CURRENT_USER;
//        try {
//            log.info("Fetching cart details for user {}", userName);
//            List<CartItem> cartItems = cartItemRepository.getCartDetails(userName);
//            List<ProductSizePrice> productSizePrices = new ArrayList<>();
//            List<CartProduct> cartProducts = new ArrayList<>();
//
//            if (!CollectionUtils.isEmpty(cartItems)) {
//                for (CartItem cartItem : cartItems){
//                    ProductSizePrice productSizePrice = new ProductSizePrice();
//                    cartItemResponseDTO.setId(cartItem.getId());
//                    CartProduct cartProduct = new CartProduct(cartItem.getProducts());
//                    cartProducts.add(cartProduct);
//                    cartItemResponseDTO.setProduct(cartProducts);
//                    productSizePrice = cartItem.getProductSizePrice();
//                    productSizePrices.add(productSizePrice);
//                    cartItemResponseDTO.setProductSizePrice(productSizePrices);
//                    cartItemResponseDTOS.add(cartItemResponseDTO);
//
//                }
//
//            }
//
//            log.info("Successfully fetched cart details for user {}", userName);
//        }catch (Exception e){
//            log.error("Error while fetching cart details for user {}", userName, e.getMessage());
//        }
//
//        return  cartItemResponseDTOS;
//
//    }





//    @Override
//    public List<CartItemResponseDTO> getCartCartDetailsFomUserToken() {
//        List<CartItemResponseDTO> cartItemResponseDTOS = new ArrayList<>();
//        String userName = JwtRequestFilter.CURRENT_USER;
//
//        try {
//            log.info("Fetching cart details for user {}", userName);
//            List<CartItem> cartItems = cartItemRepository.getCartDetails(userName);
//
//            if (!CollectionUtils.isEmpty(cartItems)) {
//                // Grouping cart items by ID so that multiple products under the same cart item are handled properly
//                Map<Integer, CartItemResponseDTO> cartMap = new HashMap<>();
//
//                for (CartItem cartItem : cartItems) {
//                    CartItemResponseDTO cartItemResponseDTO;
//
//                    // Check if the current cart item already exists
//                    if (cartMap.containsKey(cartItem.getId())) {
//                        cartItemResponseDTO = cartMap.get(cartItem.getId());
//                    } else {
//                        cartItemResponseDTO = new CartItemResponseDTO();
//                        cartItemResponseDTO.setId(cartItem.getId());
//                        cartItemResponseDTO.setProduct(new ArrayList<>());
//                        cartItemResponseDTO.setProductSizePrice(new ArrayList<>());
//                    }
//
//                    // Add product details
//                    CartProduct cartProduct = new CartProduct(cartItem.getProducts());
//                    cartItemResponseDTO.getProduct().add(cartProduct);
//
//                    // Add product size and price details
//                    ProductSizePrice productSizePrice = cartItem.getProductSizePrice();
//                    if (productSizePrice != null) {
//                        cartItemResponseDTO.getProductSizePrice().add(productSizePrice);
//                    }
//
//                    // Update the cartMap
//                    cartMap.put(cartItem.getId(), cartItemResponseDTO);
//                }
//
//                // Add all items from the map to the final list
//                cartItemResponseDTOS.addAll(cartMap.values());
//            }
//
//            log.info("Successfully fetched cart details for user {}", userName);
//        } catch (Exception e) {
//            log.error("Error while fetching cart details for user {}", userName, e);
//        }
//
//        return cartItemResponseDTOS;
//    }



    @Override
    public List<CartItemResponseDTO> getCartCartDetailsFomUserToken() {
        List<CartItemResponseDTO> cartItemResponseDTOS = new ArrayList<>();
        String userName = JwtRequestFilter.CURRENT_USER;

        try {
            log.info("Fetching cart details for user {}", userName);
            List<CartItem> cartItems = cartItemRepository.getCartDetails(userName);

            if (!CollectionUtils.isEmpty(cartItems)) {
                // Grouping cart items by ID so that multiple products under the same cart item are handled properly
                Map<Integer, CartItemResponseDTO> cartMap = new HashMap<>();

                for (CartItem cartItem : cartItems) {
                    CartItemResponseDTO cartItemResponseDTO;

                    // Check if the current cart item already exists
                    if (cartMap.containsKey(cartItem.getId())) {
                        cartItemResponseDTO = cartMap.get(cartItem.getId());
                    } else {
                        cartItemResponseDTO = new CartItemResponseDTO();
                        cartItemResponseDTO.setId(cartItem.getId());
                        cartItemResponseDTO.setProduct(new ArrayList<>());
                        cartItemResponseDTO.setProductSizePrice(new ArrayList<>());
                    }

                    CartProduct cartProduct = new CartProduct(cartItem.getProducts());
                    cartItemResponseDTO.getProduct().add(cartProduct);
                    ProductSizePrice productSizePrice = cartItem.getProductSizePrice();
                    if (productSizePrice != null) {
                        productSizePrice.setQty(cartItem.getQuantity());
                        cartItemResponseDTO.getProductSizePrice().add(productSizePrice);
                    }

                    cartMap.put(cartItem.getId(), cartItemResponseDTO);
                }
                cartItemResponseDTOS.addAll(cartMap.values());
            }

            log.info("Successfully fetched cart details for user {}", userName);
        } catch (Exception e) {
            log.error("Error while fetching cart details for user {}", userName, e);
        }

        return cartItemResponseDTOS;
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

//
//
//        Optional<CartItemDetails> existingCartItem = cartDetails.getCartItemDetails().stream()
//                .filter(item ->
//                        item.getProduct().getId().equals(product.getId()) &&
//                                item.getSelectedSize().getValue().equals(selectedSize.getValue()))
//                .findFirst();
//
//
//        if (existingCartItem.isPresent()) {
//            // Update quantity if item exists
//            CartItemDetails item = existingCartItem.get();
//            item.setQuantity(item.getQuantity() + cartRequestDTO.getQuantity());
//        } else {
//            // Create new cart item
//            CartItemDetails newCartItem = new CartItemDetails();
//            newCartItem.setProduct(product);
//            newCartItem.setSelectedSize(selectedSize);
//            newCartItem.setQuantity(cartRequestDTO.getQuantity());
//            cartDetails.getCartItemDetails().add(newCartItem);
//        }

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
    public CartDetails getItem() throws Exception {
        String userName = JwtRequestFilter.CURRENT_USER;
        User user  = userRepository.findById(userName).orElseThrow( () -> new Exception("User Not Found"));
       CartDetails cartDetails = cartDetailsRepository.findByUser(user);
        if (cartDetails == null) {
            cartDetails = new CartDetails(user);
        }

        // Ensure only the primary images are returned for products in the cart
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
//        cartDetails.getCartItemDetails().forEach(cartItem -> {
//            cartItem.getProduct().setRating(null);
//            cartItem.getProduct().setReviews(null);
//            ProductDetails product = cartItem.getProduct();
//            ProductSize selectedSize = cartItem.getSelectedSize();
//
//            List<ProductSize> filteredSizes = product.getSizes().stream()
//                    .filter(size -> size.getValue().equals(selectedSize.getValue()))
//                    .collect(Collectors.toList());
//            product.setSizes(filteredSizes);
//        });
        cartDetails.setUser(null);
//        cartDetails.getCartItemDetails().forEach(cart-> {
//            cart.getProduct().setRating(null);
//            cart.getProduct().setReviews(null);
//
//        });

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


}
