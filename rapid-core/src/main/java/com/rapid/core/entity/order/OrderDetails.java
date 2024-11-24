package com.rapid.core.entity.order;

import com.rapid.core.entity.User;
import com.rapid.core.entity.product.Products;
import com.rapid.core.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "order_details")
@NoArgsConstructor
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer orderId;

    @Column(name = "name")
    private String orderName;

    @Column(name = "phone")
    private Long orderPhone;

    @Column(name = "alternate_phone")
    private Long orderAlterNatePhone;

    @Column(name = "email")
    private String orderEmail;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @Column(name = "total_price")
    private Double totalPrice;


    @Column(name = "shipping_address",length = 1000)
    private String shippingAddress;

    @Column(name = "order_date")
    private Date orderDate;

    private String order_status;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products product;

    @Column(name = "delivery_fee")
    private Double deliveryFee;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public OrderDetails(String orderName, Long orderPhone, Long orderAlterNatePhone,
                        String orderEmail, Integer totalQuantity, String  shippingAddress , Date orderDate,
                        Double totalPrice, String order_status, Products product, User user) {
        this.orderName = orderName;
        this.orderPhone = orderPhone;
        this.orderAlterNatePhone = orderAlterNatePhone;
        this.orderEmail = orderEmail;
        this.totalQuantity = totalQuantity;
        this.shippingAddress = shippingAddress;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.order_status = order_status;
        this.product = product;
        this.user = user;
    }




}
