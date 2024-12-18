package com.rapid.core.order;

import com.rapid.core.entity.UserAddress;
import com.rapid.core.enums.PaymentMethod;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@Getter
@Setter
public class OrderRequest {

    @NotNull
    private String userEmail;

    @NotNull
    private UserAddress shippingAddress;

    @NotNull
    private UserAddress billingAddress;

    @NotNull
    private PaymentMethod paymentMethod;

    @NotNull
    private double subTotalPrice;

    @NotNull
    private double totalPrice;

    @NotNull
    private double shippingCost;


    @NotNull
    @Size(min = 1)
    private List<OrderItemRequest> items;
}
