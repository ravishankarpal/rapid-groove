package com.rapid.core.entity;

import com.rapid.core.entity.order.Cart;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;


@Entity
@Getter
@Setter
@Table(name = "user")
public class User {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;


    @Column(name = "password")
    private String password;

    @Column(name = "auth_provider")
    private String authProvider;

    @Column(name = "email_verified")
    private boolean emailVerified;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
//    private Cart cart;

    // many user might have many roles
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLE",
    joinColumns = {
            @JoinColumn(name = "USER_ID")
    },
    inverseJoinColumns = {
            @JoinColumn(name = "ROLE_ID")
    })
    private Set<Role> role;

}
