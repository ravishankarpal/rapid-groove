package com.rapid.service;

import com.rapid.core.dto.OrderDto;
import com.rapid.core.entity.User;
import com.rapid.core.entity.order.OrderDetails;
import com.rapid.core.dto.OrderProductQuantityDto;
import com.rapid.core.entity.product.Products;
import com.rapid.core.enums.OrderStatus;
import com.rapid.dao.OrderRepository;
import com.rapid.dao.ProductRepository;
import com.rapid.dao.UserRepository;
import com.rapid.security.JwtRequestFilter;
import com.rapid.service.exception.ProductDetailsNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    public void placeOrder(OrderDto orderDto) {
            String currentUser = JwtRequestFilter.CURRENT_USER;
            Optional<User> user = userRepository.findById(currentUser);
            if (user.isPresent()) {
                List<OrderProductQuantityDto> orderProductQuantities = orderDto.getOrderProductQuantities();
                for (OrderProductQuantityDto orderProductQuantityDto : orderProductQuantities) {
                    productRepository.findById(orderProductQuantityDto.getProductId()).ifPresentOrElse(
                            product -> {
                                log.info("Going to place an order for user :{}", currentUser);
                                OrderDetails orderDetail = createOrderDetails(orderDto, product, user.get(), orderProductQuantityDto);
                                orderRepository.saveAndFlush(orderDetail);
                                log.info("Order for product: {}  by user: {} has been placed successfully!  " +
                                                "Order details : [ID: {}, Quantity: {}, Total Price: {}]",
                                        product.getProductName(), currentUser, orderDetail.getOrderId(),
                                        orderDetail.getTotalQuantity(), orderDetail.getTotalPrice());
                            },
                            () -> {
                                throw new ProductDetailsNotFoundException("Product details not found for product ID: " +
                                        orderProductQuantityDto.getProductId());
                            }
                    );
                }
            }else {
                throw new UsernameNotFoundException("Invalid user");
            }
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
