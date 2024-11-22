package com.rapid.service;


import com.rapid.core.dto.*;
import com.rapid.core.entity.CheckoutItem;
import com.rapid.core.entity.CheckoutRequest;
import com.rapid.core.entity.User;
import com.rapid.core.entity.order.Cart;
import com.rapid.core.entity.order.CartItem;
import com.rapid.core.entity.product.ProductSizePrice;
import com.rapid.core.entity.product.Products;
import com.rapid.dao.*;
import com.rapid.security.JwtRequestFilter;
import com.rapid.security.JwtTokenDetails;
import com.rapid.service.exception.ProductDetailsNotFoundException;
import com.rapid.service.exception.RapidGrooveException;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;


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
        checkoutRequestRepository.saveAndFlush(checkout);
        log.info("Successfully checkout by for user {}:", user.getEmail());



    }

    @Override
    public CheckoutResponse getCheckoutDetails() throws RapidGrooveException {
        String currentUser = JwtRequestFilter.CURRENT_USER;
        User user = userRepository.findById(currentUser).orElseThrow(()-> new RapidGrooveException("User not found!"));
        Object[] objects =  checkoutRequestRepository.findByUserId(user.getEmail());
        CheckoutResponse checkoutResponse = new CheckoutResponse();
        if(objects == null ||objects.length==0 ) {
            log.info("No details found");
        }
        Object obj = objects[0];

        if (obj instanceof Object[]) {
            Object[] row = (Object[]) obj; // Cast the first row to Object[]

            // Set values in CheckoutResponse
            checkoutResponse.setProductId((Integer) row[0]);          // product_id
            checkoutResponse.setProductName((String) row[1]);         // product_name
            checkoutResponse.setQuantity((Integer) row[2]);           // quantity
            checkoutResponse.setSize((String) row[3]);                // size
            checkoutResponse.setPrice((BigDecimal) row[4]);           // price
            checkoutResponse.setPicByte((byte[]) row[5]);             // pic_byte
            checkoutResponse.setTotalAmount((BigDecimal) row[6]);     // total_amount
            checkoutResponse.setDiscountAmount((Double) row[7]);  // discount_amount
        } else {
            log.error("Unexpected data structure: " + obj.getClass());
        }


//        checkoutResponse.setProductId();
//            checkoutResponse.setProductName(((String) obj[1]).toString());
//            checkoutResponse.setQuantity(((BigInteger) obj[2]).intValue());
//            checkoutResponse.setSize(((String) obj[3]).toString());
//            checkoutResponse.setPrice(((BigDecimal) obj[4]));
//            //checkoutResponse.setPicByte();
//            checkoutResponse.setTotalAmount(((BigDecimal) obj[6]));
//            checkoutResponse.setTotalAmount(((BigDecimal) obj[7]));




        return checkoutResponse;

    }


}
