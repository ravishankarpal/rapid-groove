package com.rapid.service;

import com.rapid.core.dto.Constant;
import com.rapid.core.dto.OrderDto;
import com.rapid.core.dto.orders.OrderExtend;
import com.rapid.core.dto.orders.CashFreeOrderResponse;
import com.rapid.core.dto.orders.OrderRequest;
import com.rapid.core.dto.orders.OrderResponse;
import com.rapid.core.dto.product.ProductItem;
import com.rapid.core.entity.UserAddress;
import com.rapid.core.entity.order.OrderProductDetails;
import com.rapid.core.enums.EndPoint;
import com.rapid.core.entity.ConfigurationKeys;
import com.rapid.core.entity.User;
import com.rapid.core.entity.order.OrderDetails;
import com.rapid.core.dto.OrderProductQuantityDto;
import com.rapid.core.enums.OrderStatus;
import com.rapid.dao.*;
import com.rapid.security.JwtRequestFilter;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j


public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

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


    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private ImageModelRepository imageModelRepository;

    @Override
    public void placeOrder(OrderDto orderDto, boolean isSingleCartCheckOut) throws MessagingException, IOException {
            String currentUser = JwtRequestFilter.CURRENT_USER;
            Optional<User> user = userRepository.findById(currentUser);
            List<OrderDetails> orderDetails = new ArrayList<>();
            boolean isOrderPlaced = false;
            if (user.isPresent()) {
                List<OrderProductQuantityDto> orderProductQuantities = orderDto.getOrderProductQuantities();
//                for (OrderProductQuantityDto orderProductQuantityDto : orderProductQuantities) {
//                    pr.findById(orderProductQuantityDto.getProductId()).ifPresentOrElse(
//                            product -> {
//                                log.info("Going to place an order for user :{}", currentUser);
////                                OrderDetails orderDetail = createOrderDetails(orderDto, product, user.get(), orderProductQuantityDto);
//                                OrderDetails orderDetail = new OrderDetails();
//                                // clearing the cart After order placed
//                                if(!isSingleCartCheckOut){                                 cartItemRepository.getCartDetails(currentUser);
//                                   List<Cart> carts =  cartRepository.findCartByUserId(currentUser);
//                                    List<Integer> cartId = carts.stream()
//                                            .map(Cart::getId)
//                                            .toList();
//                                   // cartRepository.deleteByUser_UserName(currentUser);
//                                    cartRepository.deleteByUser_Email(currentUser);
//                                   cartItemRepository.deleteAllById(cartId);
//                                }
//                                orderDetails.add(orderDetail);
//
//                            },
//                            () -> {
//                                throw new ProductDetailsNotFoundException("Product details not found for product ID: " +
//                                        orderProductQuantityDto.getProductId());
//                            }
//                    );
//                }
                if (!orderDetails.isEmpty()){
                    orderRepository.saveAllAndFlush(orderDetails);

                    //emailService.sendOrderConfirmationEmail(orderDetails);
                    log.info("Email has been successfully sent to user :{}",currentUser);
                }
            }else {
                throw new UsernameNotFoundException("Invalid user");
            }
    }




    @Override
    public CashFreeOrderResponse createOrder(OrderRequest paymentRequest) throws Exception {
        log.info("going to create an order");
        String userName = JwtRequestFilter.CURRENT_USER;
        HttpHeaders httpHeaders  = getCashFreeHeaders();
        StringBuilder apiUrl = new StringBuilder();

        apiUrl.append(cashFreeBaseUrl).append(EndPoint.PG_ORDERS.getEndPoint());

        Integer userAddressId = Integer.valueOf(paymentRequest.getCustomerDetails().getCustomerId());
        UserAddress userAddress = userAddressRepository.findById(userAddressId).orElseThrow(() -> new  Exception("Address not found"));
        HttpEntity<OrderRequest> entity = new HttpEntity<>(paymentRequest, httpHeaders);
        ResponseEntity<CashFreeOrderResponse> response  = restTemplate.exchange(apiUrl.toString(), HttpMethod.POST, entity, CashFreeOrderResponse.class);
        if (response.getStatusCode() == HttpStatus.OK){
            CashFreeOrderResponse orderResponse = response.getBody();
            OrderDetails orderDetails = new OrderDetails(orderResponse,paymentRequest);
            orderDetails.setUserAddress(userAddress);
            orderDetails.setUser(userRepository.findById(JwtRequestFilter.CURRENT_USER).orElseThrow(()-> new Exception("User not found!")));
            orderRepository.saveAndFlush(orderDetails);
            emailService.sendOrderConfirmationEmail(orderDetails);
            return orderResponse;
        }else{
            throw new Exception("Error while creating order with Cashfree");
        }


    }

    @Override
    public CashFreeOrderResponse getOrder(String orderId) throws Exception {
        HttpHeaders httpHeaders  = getCashFreeHeaders();
        StringBuilder apiUrl = new StringBuilder();
        apiUrl.append(cashFreeBaseUrl).append(EndPoint.PG_ORDERS.getEndPoint()).append("/")
                .append(orderId);

        HttpEntity<Void> entity = new HttpEntity<>( httpHeaders);
        ResponseEntity<CashFreeOrderResponse> response  = restTemplate.exchange(apiUrl.toString(), HttpMethod.GET, entity, CashFreeOrderResponse.class);
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
    public CashFreeOrderResponse terminateOrder(String orderId) throws Exception {
        HttpHeaders httpHeaders  = getCashFreeHeaders();
        StringBuilder apiUrl = new StringBuilder();
        apiUrl.append(cashFreeBaseUrl).append(EndPoint.PG_ORDERS.getEndPoint()).append("/")
                .append(orderId);

        HttpEntity<Void> entity = new HttpEntity<>( httpHeaders);
        ResponseEntity<CashFreeOrderResponse> response  = restTemplate.exchange(apiUrl.toString(), HttpMethod.PATCH, entity, CashFreeOrderResponse.class);
        if (response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }else{
            throw new Exception("Error while creating order with Cashfree");
        }
    }

    @Override
    public CashFreeOrderResponse getOrderExtend(String orderId) throws Exception {
        HttpHeaders httpHeaders  = getCashFreeHeaders();
        StringBuilder apiUrl = new StringBuilder();
        apiUrl.append(cashFreeBaseUrl).append(EndPoint.PG_ORDERS.getEndPoint()).append("/")
                .append(orderId).append("/extended");

        HttpEntity<Void> entity = new HttpEntity<>( httpHeaders);
        ResponseEntity<CashFreeOrderResponse> response  = restTemplate.exchange(apiUrl.toString(), HttpMethod.GET, entity, CashFreeOrderResponse.class);
        if (response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }else{
            throw new Exception("Error while creating order with Cashfree");
        }
    }


    @Override
    public CashFreeOrderResponse updateOrderExtend(String orderId, OrderExtend orderExtend) throws Exception {
        HttpHeaders httpHeaders  = getCashFreeHeaders();
        StringBuilder apiUrl = new StringBuilder();
        apiUrl.append(cashFreeBaseUrl).append(EndPoint.PG_ORDERS.getEndPoint()).append("/")
                .append(orderId).append("/extended");

        HttpEntity<OrderExtend> entity = new HttpEntity<>( orderExtend,httpHeaders);
        ResponseEntity<CashFreeOrderResponse> response  = restTemplate.exchange(apiUrl.toString(), HttpMethod.PUT, entity, CashFreeOrderResponse.class);
        if (response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }else{
            throw new Exception("Error while creating order with Cashfree");
        }
    }

    @Override
    public  void sendOrderConfirmationEmail(String orderId) throws MessagingException, IOException {
      OrderDetails orderDetails =   orderRepository.findByOrderId(orderId);
      emailService.sendOrderConfirmationEmail(orderDetails);
    }

    @Override
    public Page<OrderResponse> getOrders( String period, OrderStatus status, int page, int size) {
        String userId = JwtRequestFilter.CURRENT_USER;
        LocalDateTime startDate = null;
        if (period != null) {
            startDate = switch (period) {
                case "3months" -> LocalDateTime.now().minusMonths(3);
                case "6months" -> LocalDateTime.now().minusMonths(6);
                case "1year" -> LocalDateTime.now().minusYears(1);
                default -> null;
            };
        }
//        Pageable pageable = PageRequest.of(page, size, Sort.by("created_at").descending());
        Pageable pageable = PageRequest.of(page, size);

        //Page<OrderDetails> orders = orderRepository.searchOrders(userId, startDate, status, pageable);
        //Page<OrderDetails> orders = orderRepository.searchOrders(userId,  pageable);
        Page<OrderDetails> orders = orderRepository.searchOrders(userId, startDate, pageable);


        List<OrderResponse> responses = new ArrayList<>();
        if(orders.hasContent()){
            List<OrderDetails> orderDetails = orders.getContent();
            for (OrderDetails details : orderDetails){

                OrderResponse orderResponse = new OrderResponse(details);
                List<OrderProductDetails> orderProductDetails = details.getOrderProducts();
                for (OrderProductDetails productDetails : orderProductDetails){
                   Integer productId =  Integer.valueOf(productDetails.getProductId());
                   productId = 3;

                    byte[] imageByte= imageModelRepository.findImageByProductId(productId);
                    List<ProductItem> productItems = orderResponse.getItems();
                    for (ProductItem item : productItems){
                        item.setImage(imageByte);
                    }
                }

                orderResponse.getShippingAddress().setUser(null);
                responses.add(orderResponse);
            }
        }

        return new PageImpl<>(responses, pageable, orders.getTotalElements());

    }

    @Override
    public OrderResponse getOrderDetailsById(String id) {
        log.info("Start fetching order details for id {}", id);
        OrderDetails orderDetails = orderRepository.findByOrderId(id);
        OrderResponse orderResponse = new OrderResponse(orderDetails);
        List<OrderProductDetails> orderProductDetails = orderDetails.getOrderProducts();
        for (OrderProductDetails productDetails : orderProductDetails){
            Integer productId =  Integer.valueOf(productDetails.getProductId());
            productId = 3;

            byte[] imageByte= imageModelRepository.findImageByProductId(productId);
            List<ProductItem> productItems = orderResponse.getItems();
            for (ProductItem item : productItems){
                item.setImage(imageByte);
            }
        }
        orderResponse.setTrackingInfo(null);

        orderResponse.getShippingAddress().setUser(null);
        log.info("Successfully fetched order details for id {}", id);
        return orderResponse;

    }

}
