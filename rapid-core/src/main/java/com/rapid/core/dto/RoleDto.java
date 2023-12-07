package com.rapid.core.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {

    private Integer id;

    private String roleName;

    private String roleDescription;
}
