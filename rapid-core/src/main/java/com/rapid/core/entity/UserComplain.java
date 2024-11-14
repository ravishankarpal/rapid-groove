package com.rapid.core.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "user_complains")
@Getter
@Setter

public class UserComplain implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "order_number",nullable = false)
    private String orderNumber;

    @Column(name = "department",nullable = false)
    private String department;

    @Column(name = "subject",nullable = false)

    private String subject;

    @Column(name = "message",nullable = false)
    private String message;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment_id", referencedColumnName = "id")
    private FileAttachment fileAttachment;
}
