package com.rapid.core.entity.checkout;

import com.rapid.core.dto.checkout.CartSummaryRequest;
import com.rapid.core.dto.checkout.CheckoutItemRequest;
import com.rapid.core.dto.checkout.CheckoutRequest;
import com.rapid.core.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "checkout_details")
public class CheckoutDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Checkout Item fields
//    @ElementCollection
//    @CollectionTable(name = "checkout_items", joinColumns = @JoinColumn(name = "checkout_details_id"))
//    @Column(name = "item")
//    @Cascade(org.hibernate.annotations.CascadeType.ALL)
//    private List<CheckoutItemEntity> itemRequests = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "checkout_items",
            joinColumns = @JoinColumn(name = "checkout_details_id"),
            foreignKey = @ForeignKey(
                    name = "FK_checkout_items_checkout_details",
                    foreignKeyDefinition = "FOREIGN KEY (checkout_details_id) REFERENCES checkout_details(id) ON DELETE CASCADE"
            )
    )
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<CheckoutItemEntity> itemRequests = new ArrayList<>() ;


    // Cart summary fields
    @Column(name = "subtotal", nullable = false)
    private double subtotal;

    @Column(name = "discount", nullable = false)
    private double discount;

    @Column(name = "delivery_fee", nullable = false)
    private double deliveryFee;

    @Column(name = "total", nullable = false)
    private double total;

    @ManyToOne(fetch = FetchType.LAZY)  // Many checkout details for one user
    @JoinColumn(name = "user_id", nullable = false)  // Foreign key column to reference User
    private User user;


    public CheckoutDetails(CheckoutRequest checkoutRequest, User user) {
        for (CheckoutItemRequest checkoutItemRequest : checkoutRequest.getItemRequests()){
            CheckoutItemEntity checkoutItemEntity = new CheckoutItemEntity(checkoutItemRequest);
            this.itemRequests.add(checkoutItemEntity);
        }
        CartSummaryRequest cartSummaryRequest = checkoutRequest.getCartSummaryRequest();
        this.subtotal = cartSummaryRequest.getSubtotal();
        this.discount = cartSummaryRequest.getDiscount();
        this.deliveryFee = cartSummaryRequest.getDeliveryFee();
        this.total = cartSummaryRequest.getTotal();
        this.user = user;
    }
}
