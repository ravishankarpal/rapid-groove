package com.rapid.service;

import com.rapid.core.dto.Constant;
import com.rapid.core.dto.OrderDto;
import com.rapid.core.dto.cart.CartDetail;
import com.rapid.core.dto.orders.OrderExtend;
import com.rapid.core.dto.orders.OrderResponse;
import com.rapid.core.dto.payment.PaymentRequest;
import com.rapid.core.entity.order.OrderCartDetails;
import com.rapid.core.enums.EndPoint;
import com.rapid.core.entity.ConfigurationKeys;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

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

    @Value("${cashfree.base.url}")
    private String cashFreeBaseUrl;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private ConfigurationKeyRepo configurationKeyRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void placeOrder(OrderDto orderDto, boolean isSingleCartCheckOut) throws MessagingException, IOException {
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
//                                OrderDetails orderDetail = createOrderDetails(orderDto, product, user.get(), orderProductQuantityDto);
                                OrderDetails orderDetail = new OrderDetails();
                                // clearing the cart After order placed
                                if(!isSingleCartCheckOut){                                 cartItemRepository.getCartDetails(currentUser);
                                   List<Cart> carts =  cartRepository.findCartByUserId(currentUser);
                                    List<Integer> cartId = carts.stream()
                                            .map(Cart::getId)
                                            .toList();
                                   // cartRepository.deleteByUser_UserName(currentUser);
                                    cartRepository.deleteByUser_Email(currentUser);
                                   cartItemRepository.deleteAllById(cartId);
                                }
                                orderDetails.add(orderDetail);

                            },
                            () -> {
                                throw new ProductDetailsNotFoundException("Product details not found for product ID: " +
                                        orderProductQuantityDto.getProductId());
                            }
                    );
                }
                if (!orderDetails.isEmpty()){
                    orderRepository.saveAllAndFlush(orderDetails);
//                    for(OrderDetails order : orderDetails) {
//                        log.info("Order for product: {}  by user: {} has been placed successfully!  " +
//                                        "Order details : [ID: {}, Quantity: {}, Total Price: {}]",
//                                order.getProduct().getProductName(), currentUser, order.getOrderId(),
//                                order.getTotalQuantity(), order.getTotalPrice());
//                    }
                    emailService.sendOrderConfirmationEmail(orderDetails);
                    log.info("Email has been successfully sent to user :{}",currentUser);
                }
            }else {
                throw new UsernameNotFoundException("Invalid user");
            }
    }

//    private OrderDetails createOrderDetails(OrderDto orderDto, Products product, User user,
//                                            OrderProductQuantityDto orderProductQuantityDto) {
//        return new OrderDetails(
//                orderDto.getFullName(),
//                orderDto.getPhoneNumber(),
//                orderDto.getAlternatePhoneNumber(),
//                orderDto.getEmail(),
//                orderProductQuantityDto.getQuantity(),
//                orderDto.getShippingAddress(),
//                new Date(),
//                (product.getProductActualPrice() *
//                        orderProductQuantityDto.getQuantity()) -
//                        (product.getProductDiscountPrice()*orderProductQuantityDto.getQuantity()),
//                OrderStatus.ORDER_PLACED.getStatus(),
//                product,
//                user
//        );
//    }


    @Override
    public OrderResponse createOrder(PaymentRequest paymentRequest) throws Exception {
        log.info("going to create an order");
        String userName = JwtRequestFilter.CURRENT_USER;
        HttpHeaders httpHeaders  = getCashFreeHeaders();
        StringBuilder apiUrl = new StringBuilder();
        apiUrl.append(cashFreeBaseUrl).append(EndPoint.PG_ORDERS.getEndPoint());

        HttpEntity<PaymentRequest> entity = new HttpEntity<>(paymentRequest, httpHeaders);
        ResponseEntity<OrderResponse> response  = restTemplate.exchange(apiUrl.toString(), HttpMethod.POST, entity, OrderResponse.class);
        if (response.getStatusCode() == HttpStatus.OK){
            OrderResponse orderResponse = response.getBody();
            OrderDetails orderDetails = new OrderDetails(orderResponse,paymentRequest);
            orderDetails.setUser(userRepository.findById(JwtRequestFilter.CURRENT_USER).orElseThrow(()-> new Exception("User not found!")));
            CartDetail cartDetail =  paymentRequest.getCartDetails();
            OrderCartDetails orderCartDetails = new OrderCartDetails(cartDetail);
            orderDetails.setCartDetails(orderCartDetails);
            orderRepository.saveAndFlush(orderDetails);
            return orderResponse;
        }else{
            throw new Exception("Error while creating order with Cashfree");
        }


    }

    @Override
    public OrderResponse getOrder(String orderId) throws Exception {
        HttpHeaders httpHeaders  = getCashFreeHeaders();
        StringBuilder apiUrl = new StringBuilder();
        apiUrl.append(cashFreeBaseUrl).append(EndPoint.PG_ORDERS.getEndPoint()).append("/")
                .append(orderId);

        HttpEntity<Void> entity = new HttpEntity<>( httpHeaders);
        ResponseEntity<OrderResponse> response  = restTemplate.exchange(apiUrl.toString(), HttpMethod.GET, entity, OrderResponse.class);
        if (response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }else{
            throw new Exception("Error while creating order with Cashfree");
        }
    }

    @Override
    public HttpHeaders getCashFreeHeaders() throws Exception {
        ConfigurationKeys  clientIdAndSecretKey = configurationKeyRepo.findByName(Constant.CASHFREE_CLIENT_ID_AND_SECRET)
                .orElseThrow(() -> new Exception("Keys are not present"));
        String clientAndSecret[] = clientIdAndSecretKey.getValue().split(",");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set("x-client-id", clientAndSecret[0]);
        httpHeaders.set("x-client-secret", clientAndSecret[1]);
        httpHeaders.set("x-api-version", "2023-08-01");
        return httpHeaders;

    }


    @Override
    public OrderResponse terminateOrder(String orderId) throws Exception {
        HttpHeaders httpHeaders  = getCashFreeHeaders();
        StringBuilder apiUrl = new StringBuilder();
        apiUrl.append(cashFreeBaseUrl).append(EndPoint.PG_ORDERS.getEndPoint()).append("/")
                .append(orderId);

        HttpEntity<Void> entity = new HttpEntity<>( httpHeaders);
        ResponseEntity<OrderResponse> response  = restTemplate.exchange(apiUrl.toString(), HttpMethod.PATCH, entity, OrderResponse.class);
        if (response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }else{
            throw new Exception("Error while creating order with Cashfree");
        }
    }

    @Override
    public OrderResponse getOrderExtend(String orderId) throws Exception {
        HttpHeaders httpHeaders  = getCashFreeHeaders();
        StringBuilder apiUrl = new StringBuilder();
        apiUrl.append(cashFreeBaseUrl).append(EndPoint.PG_ORDERS.getEndPoint()).append("/")
                .append(orderId).append("/extended");

        HttpEntity<Void> entity = new HttpEntity<>( httpHeaders);
        ResponseEntity<OrderResponse> response  = restTemplate.exchange(apiUrl.toString(), HttpMethod.GET, entity, OrderResponse.class);
        if (response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }else{
            throw new Exception("Error while creating order with Cashfree");
        }
    }


    @Override
    public OrderResponse updateOrderExtend(String orderId, OrderExtend orderExtend) throws Exception {
        HttpHeaders httpHeaders  = getCashFreeHeaders();
        StringBuilder apiUrl = new StringBuilder();
        apiUrl.append(cashFreeBaseUrl).append(EndPoint.PG_ORDERS.getEndPoint()).append("/")
                .append(orderId).append("/extended");

        HttpEntity<OrderExtend> entity = new HttpEntity<>( orderExtend,httpHeaders);
        ResponseEntity<OrderResponse> response  = restTemplate.exchange(apiUrl.toString(), HttpMethod.PUT, entity, OrderResponse.class);
        if (response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }else{
            throw new Exception("Error while creating order with Cashfree");
        }
    }


}
