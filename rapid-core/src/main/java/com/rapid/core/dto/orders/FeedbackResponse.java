package com.rapid.core.dto.orders;

import com.rapid.core.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {
    private String orderId;
    private FeedbackRequest feedback;
    private OrderStatus status;
}
