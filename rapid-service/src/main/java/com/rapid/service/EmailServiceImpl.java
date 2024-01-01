package com.rapid.service;

import com.rapid.core.entity.order.OrderDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements  EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    String from;
    @Override
    public void sendOrderConfirmationEmail(List<OrderDetails> orderDetails) throws MessagingException {
        OrderDetails orderDetail = orderDetails.get(0);
        String to = orderDetail.getOrderEmail();
        String subject = "Order Confirmation";
        String body = buildOrderConfirmationEmailBody(orderDetails);
        sendOrderConfirmationEmail(to, subject, body);

    }

    public void sendOrderConfirmationEmail(String to, String subject,String  body) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        javaMailSender.send(mimeMessage);
    }





    private String buildOrderConfirmationEmailBody(List<OrderDetails> orderDetails) {
        StringBuilder body = new StringBuilder("<html><body><p>Thank you for placing your order! Your order details are:</p>");

        // Table opening tag
        body.append("<table border=\"1\">");

        // Table header
        body.append("<tr><th>Item</th><th>Quantity</th><th>Price</th></tr>");

        // Table rows
        for (OrderDetails order : orderDetails) {
            body.append("<tr>");
            body.append("<td>").append(order.getProduct().getProductName()).append("</td>");
            body.append("<td>").append(order.getTotalQuantity()).append("</td>");
            body.append("<td>").append(order.getTotalPrice()).append("</td>");
            body.append("</tr>");
        }

        body.append("<tr><td colspan=\"2\" style=\"text-align:center\"><b>Total Price</b></td>");
        body.append("<td>").append(calculateTotalPrice(orderDetails)).append("</td></tr>");

        // Table closing tag
        body.append("</table></body></html>");

        return body.toString();
    }

    private Double calculateTotalPrice(List<OrderDetails> orderDetails) {

        return orderDetails.stream()
                .mapToDouble(OrderDetails::getTotalPrice)
                .sum();

    }
}
