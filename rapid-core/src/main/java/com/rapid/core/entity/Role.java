package com.rapid.core.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // or GenerationType.AUTO if applicable
    @Column(name = "id")
    private Integer id;
    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_desc")
    private String roleDescriptions;

}
