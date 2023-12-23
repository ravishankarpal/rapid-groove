package com.rapid.core.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderDto {

    private String fullName;

    private String shippingAddress;

    private Long phoneNumber;

    private Long alternatePhoneNumber;

    private String email;

    private List<OrderProductQuantityDto> orderProductQuantities;

}
