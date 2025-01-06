package com.rapid.core.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rapid.core.entity.product.ProductSizePrice;
import com.rapid.core.entity.product.Products;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "cart_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"cart"})

public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer  id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;

    @Column(name = "size")
    private Integer size;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products products;


    @ManyToOne
    @JoinColumn(name = "size_price_id", nullable = false)
    private ProductSizePrice  productSizePrice;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;




}
