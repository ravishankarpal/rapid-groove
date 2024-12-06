package com.rapid.core.entity.cart;

import com.rapid.core.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cart_details")
@Getter
@Setter
@NoArgsConstructor
public class CartDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_item_id")
    private List<CartItemDetails> cartItemDetails = new ArrayList<>();

    @Column(name = "total_price",nullable = false)
    private Double totalPrice;

    @Column(name = "total_items",nullable = false)
    private Integer totalItems;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    public CartDetails(User user) {
        this.user = user;
        this.cartItemDetails = new ArrayList<>();
    }


    @PrePersist
    @PreUpdate
    public void calculateTotals() {
        this.totalItems = cartItemDetails.stream()
                .mapToInt(CartItemDetails::getQuantity)
                .sum();

        this.totalPrice = cartItemDetails.stream()
                .mapToDouble(item ->
                        item.getSelectedSize().getPrice().getCurrent() * item.getQuantity())
                .sum();

        if (this.createdAt == null) {
            this.createdAt = new Date();
        }
        this.updatedAt = new Date();
    }
}
