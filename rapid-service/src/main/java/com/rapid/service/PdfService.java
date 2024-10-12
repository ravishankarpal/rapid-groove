package com.rapid.service;

import com.rapid.core.dto.BillingDto;
import com.rapid.core.entity.order.OrderDetails;

import java.io.IOException;
import java.util.List;

public interface PdfService {
     byte[] generateInvoice(List<OrderDetails> orderDetails) throws IOException;
}
