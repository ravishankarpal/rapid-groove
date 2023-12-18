package com.rapid.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_first_name")
    private String userFirstName;

    @Column(name = "user_last_name")
    private String userLastName;


    @Column(name = "user_password")
    private String userPassword;

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
