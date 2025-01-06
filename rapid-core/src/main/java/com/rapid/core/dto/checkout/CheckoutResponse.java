package com.rapid.core.dto.checkout;

import com.rapid.core.entity.checkout.CheckoutDetails;
import com.rapid.core.entity.checkout.CheckoutItemEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponse {

    private List<CheckoutItemResponse> itemResponses;
    private CartSummaryResponse cartSummaryResponse;


}
