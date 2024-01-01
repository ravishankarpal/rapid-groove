package com.rapid.service;

import com.rapid.core.entity.order.OrderDetails;
import jakarta.mail.MessagingException;

import java.util.List;

public interface EmailService {

   void sendOrderConfirmationEmail(List<OrderDetails> orderDetails) throws MessagingException;
}
