package com.rapid.core.dto.checkout;

import com.rapid.core.entity.checkout.CheckoutDetails;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Data
public class CheckoutRequestResponse {
    private List<Integer> productId;
    private List<String> productSize;


}
