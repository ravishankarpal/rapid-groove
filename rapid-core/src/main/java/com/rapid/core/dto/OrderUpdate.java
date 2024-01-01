package com.rapid.core.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderUpdate {

    Integer orderId;

    String orderStatus;
}
