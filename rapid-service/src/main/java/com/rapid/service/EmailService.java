package com.rapid.service;

import com.rapid.core.entity.User;
import com.rapid.core.entity.order.OrderDetails;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.List;

public interface EmailService {

   void sendOrderConfirmationEmail(List<OrderDetails> orderDetails) throws MessagingException, IOException;

   void sendOTPEmail(User user, String otp) throws MessagingException;
}
