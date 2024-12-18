package com.rapid.service.orders;

import com.rapid.core.entity.User;
import com.rapid.core.entity.order.OrderDetail;
import com.rapid.core.entity.order.OrderItem;
import com.rapid.core.entity.product.ProductDetails;
import com.rapid.core.entity.product.ProductSize;
import com.rapid.core.enums.OrderStatus;
import com.rapid.core.exception.OrderNotFoundException;
import com.rapid.core.exception.ProductNotFoundException;
import com.rapid.core.exception.UserNotFoundException;
import com.rapid.core.order.OrderItemRequest;
import com.rapid.core.order.OrderRequest;
import com.rapid.dao.OrderDetailRepository;
import com.rapid.dao.ProductDetailsRepository;
import com.rapid.dao.UserRepository;
import com.rapid.security.JwtRequestFilter;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductDetailsRepository productDetailsRepository;


    @Transactional
    @Override
    public OrderDetail createOrder(OrderRequest orderRequest) {
        String userName = JwtRequestFilter.CURRENT_USER;
        User user = userRepository.findById(userName)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setUser(user);
        orderDetail.setShippingAddress(orderRequest.getShippingAddress());
        orderDetail.setBillingAddress(orderRequest.getBillingAddress());
        orderDetail.setOrderStatus(OrderStatus.PENDING);
        orderDetail.setOrderDate(LocalDateTime.now());
        orderDetail.setPaymentMethod(orderRequest.getPaymentMethod());
        List<OrderItem> orderItems = createOrderItems(orderRequest.getItems(), orderDetail);
        orderDetail.setOrderItems(orderItems);
        orderDetail.setSubTotalAmount(BigDecimal.valueOf(orderRequest.getSubTotalPrice()));
        orderDetail.setTotalAmount(BigDecimal.valueOf(orderRequest.getTotalPrice()));
        orderDetail.setShippingCost(BigDecimal.valueOf(orderRequest.getShippingCost()));
        return orderDetailRepository.saveAndFlush(orderDetail);
    }


    private List<OrderItem> createOrderItems(List<OrderItemRequest> itemRequests, OrderDetail orderDetail) {
        return itemRequests.stream()
                .map(itemRequest -> {
                    ProductDetails product = productDetailsRepository.findById(itemRequest.getProductId())
                            .orElseThrow(() -> new ProductNotFoundException("Product not found"));
                    ProductSize sizeDetails = product.getSizes().stream()
                            .filter(size -> size.getValue().equals(itemRequest.getSize()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Invalid size: " + itemRequest.getSize()));
                    Double unitPrice = sizeDetails.getPrice().getOriginal();
                    Integer discountPercentage = sizeDetails.getPrice().getDiscountPercentage();
                    double discountAmount = (unitPrice * discountPercentage) / 100.0;
                    double totalAmount = (itemRequest.getQuantity() * unitPrice) - discountAmount;
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrderDetails(orderDetail);
                    orderItem.setProduct(product);
                    orderItem.setSize(itemRequest.getSize());
                    orderItem.setQuantity(itemRequest.getQuantity());
                    orderItem.setUnitPrice(unitPrice);
                    orderItem.setDiscountAmount(discountAmount);
                    orderItem.setTotalPrice(totalAmount);
                    return orderItem;
                })
                .collect(Collectors.toList());
    }

    public OrderDetail getOrderById(UUID orderId) {
        return orderDetailRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }

    @Transactional
    public OrderDetail updateOrder(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }



}
