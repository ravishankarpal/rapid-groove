package com.rapid.service;

import com.rapid.core.entity.order.OrderDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EmailServiceImpl implements  EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    String from;

    @Autowired
    private PdfService pdfService;
    @Override
    public void sendOrderConfirmationEmail(List<OrderDetails> orderDetails) throws MessagingException, IOException {
        OrderDetails orderDetail = orderDetails.get(0);
        String to = orderDetail.getOrderEmail();
        String subject = "Order Confirmation";
        String body = buildOrderConfirmationEmailBody(orderDetails);
        sendOrderConfirmationEmailHelper(to, subject, body,orderDetails);

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

    public void sendOrderConfirmationEmailHelper(String to, String subject, String body,
                                                 List<OrderDetails> orderDetails) throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        String fileName = "invoice_"+ orderDetails.get(0).getOrderId();
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setReplyTo("noreply@example.com");
        helper.setText(body, true);
        byte[] pdfAttachment = pdfService.generateInvoice(orderDetails);
        // Attach the PDF file
        helper.addAttachment(fileName, new ByteArrayResource(pdfAttachment),
                "application/pdf");

        javaMailSender.send(mimeMessage);
    }
}
