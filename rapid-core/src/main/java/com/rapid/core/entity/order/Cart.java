package com.rapid.core.entity.order;


import com.rapid.core.entity.User;
import com.rapid.core.entity.product.Products;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;

//    public Cart(Products products, User user) {
//        this.products = products;
//        this.user = user;
//    }

    public Cart(User user) {
        this.user = user;
        this.cartItems = new ArrayList<>();
    }
}
