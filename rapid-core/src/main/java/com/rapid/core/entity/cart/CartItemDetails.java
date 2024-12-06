package com.rapid.core.entity.cart;


import com.rapid.core.entity.product.ProductDetails;
import com.rapid.core.entity.product.ProductSize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cart_item_details")
@Getter
@Setter
public class CartItemDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private ProductDetails product;

    @Embedded
    private ProductSize selectedSize;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "item_total")
    private Double itemTotal;

    @PrePersist
    @PreUpdate
    public void calculateItemTotal() {
        this.itemTotal = selectedSize.getPrice().getCurrent() * quantity;
    }
}
