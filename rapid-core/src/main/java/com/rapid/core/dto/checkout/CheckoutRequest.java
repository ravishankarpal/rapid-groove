package com.rapid.core.dto.checkout;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Data
public class CheckoutRequest {
    private List<CheckoutItemRequest> itemRequests;
    private CartSummaryRequest cartSummaryRequest;
}
