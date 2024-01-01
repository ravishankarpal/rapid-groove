package com.rapid.service;

import com.rapid.core.dto.OrderDto;
import com.rapid.core.entity.User;
import com.rapid.core.entity.order.Cart;
import com.rapid.core.entity.order.OrderDetails;
import com.rapid.core.dto.OrderProductQuantityDto;
import com.rapid.core.entity.product.Products;
import com.rapid.core.enums.OrderStatus;
import com.rapid.dao.*;
import com.rapid.security.JwtRequestFilter;
import com.rapid.service.exception.ProductDetailsNotFoundException;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private EmailService emailService;
    @Override
    public void placeOrder(OrderDto orderDto, boolean isSingleCartCheckOut) throws MessagingException {
            String currentUser = JwtRequestFilter.CURRENT_USER;
            Optional<User> user = userRepository.findById(currentUser);
            List<OrderDetails> orderDetails = new ArrayList<>();
            boolean isOrderPlaced = false;
            if (user.isPresent()) {
                List<OrderProductQuantityDto> orderProductQuantities = orderDto.getOrderProductQuantities();
                for (OrderProductQuantityDto orderProductQuantityDto : orderProductQuantities) {
                    productRepository.findById(orderProductQuantityDto.getProductId()).ifPresentOrElse(
                            product -> {
                                log.info("Going to place an order for user :{}", currentUser);
                                OrderDetails orderDetail = createOrderDetails(orderDto, product, user.get(), orderProductQuantityDto);
                                // clearing the cart After order placed
                                if(!isSingleCartCheckOut){                                 cartItemRepository.getCartDetails(currentUser);
                                   List<Cart> carts =  cartRepository.findCartByUserId(currentUser);
                                    List<Integer> cartId = carts.stream()
                                            .map(Cart::getId)
                                            .toList();
                                    cartRepository.deleteByUser_UserName(currentUser);
                                   cartItemRepository.deleteAllById(cartId);
                                }
                                orderDetails.add(orderDetail);
                                //orderRepository.saveAndFlush(orderDetail);
//                                log.info("Order for product: {}  by user: {} has been placed successfully!  " +
//                                                "Order details : [ID: {}, Quantity: {}, Total Price: {}]",
//                                        product.getProductName(), currentUser, orderDetail.getOrderId(),
//                                        orderDetail.getTotalQuantity(), orderDetail.getTotalPrice());
                            },
                            () -> {
                                throw new ProductDetailsNotFoundException("Product details not found for product ID: " +
                                        orderProductQuantityDto.getProductId());
                            }
                    );
                }
                if (!orderDetails.isEmpty()){
                    orderRepository.saveAllAndFlush(orderDetails);
                    isOrderPlaced = true;
                    emailService.sendOrderConfirmationEmail(orderDetails);
                }
            }else {
                throw new UsernameNotFoundException("Invalid user");
            }
    }

    @Override
    public List<OrderDetails> getMyOrderDetails() {
        String userName = JwtRequestFilter.CURRENT_USER;
        return  orderRepository.findByUser_UserName(userName);
    }

    private OrderDetails createOrderDetails(OrderDto orderDto, Products product, User user,
                                            OrderProductQuantityDto orderProductQuantityDto) {
        return new OrderDetails(
                orderDto.getFullName(),
                orderDto.getPhoneNumber(),
                orderDto.getAlternatePhoneNumber(),
                orderDto.getEmail(),
                orderProductQuantityDto.getQuantity(),
                orderDto.getShippingAddress(),
                new Date(),
                (product.getProductActualPrice() *
                        orderProductQuantityDto.getQuantity()) -
                        (product.getProductDiscountPrice()*orderProductQuantityDto.getQuantity()),
                OrderStatus.ORDER_PLACED.getStatus(),
                product,
                user
        );
    }





}
