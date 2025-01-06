package com.rapid.service;

import com.rapid.core.dto.BillingDto;
import com.rapid.core.dto.invoice.Invoice;
import com.rapid.core.entity.order.OrderDetails;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface PdfService {
   //  byte[] generateInvoice(List<OrderDetails> orderDetails) throws IOException;


     void generatePDF(String orderId, String filePath) throws FileNotFoundException;
}
