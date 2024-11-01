package com.rapid.core.dto;

import com.rapid.core.entity.order.OrderDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillingDto implements Serializable {

    private String customerName;

    private List<OrderDetails> orderDetails;
}
