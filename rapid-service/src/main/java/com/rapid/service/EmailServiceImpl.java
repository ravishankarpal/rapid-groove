package com.rapid.service;

import com.rapid.core.dto.Constant;
import com.rapid.core.entity.User;
import com.rapid.core.entity.order.OrderDetails;
import com.rapid.core.entity.order.OrderProductDetails;
import com.rapid.dao.ImageModelRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailServiceImpl implements  EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    String from;


    @Autowired
    private ImageModelRepository imageModelRepository;

    @Autowired
    private PdfService pdfService;
    @Override
    public void sendOrderConfirmationEmail(OrderDetails orderDetails) throws MessagingException, IOException {
        //String to = orderDetails.getUser().getEmail();
        String to = "rshankarpl96@gmail.com";
        String subject = "Order Confirmation";
        String body = buildOrderConfirmationEmailBody(orderDetails);
       sendOrderConfirmationEmailHelper(to, subject, body,orderDetails);

    }

    @Override
    public void sendOTPEmail(User user, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom(from);
        helper.setTo(user.getEmail());
        helper.setSubject("[Rapid Groove] Please reset your password");
        helper.setReplyTo("noreply@example.com");
        String emailContent = "<html><body>" +
                "<h2>Rapid Groove Password Reset</h2>" +
                "<p>Hi "+user.getName()+",</p>" +
                "<p>We received a request to reset the password for your Rapid Groove account.</p>" +
                "<p>Your OTP (One-Time Password) for resetting your password is:</p>" +
                "<h3 style=\"color: #2d87f0;\">" + otp + "</h3>" +
                "<p>Please use this OTP within <strong>" + Constant.OTP_VALIDITY_MINUTES + " minutes</strong> as it will expire after that.</p>" +
                "<p>If you did not request a password reset, please ignore this email or contact our support team.</p>" +
                "<br>" +
                "<p>Thank you,</p>" +
                "<p>The Rapid Groove Team</p>" +
                "</body></html>";
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);

    }

    private String buildOrderConfirmationEmailBody(OrderDetails orderDetails) {
        StringBuilder body = new StringBuilder();
        body.append("""
        <html>
        <head>
            <style>
                table { border-collapse: collapse; width: 100%; margin-top: 20px; }
                th, td { padding: 12px; text-align: left; border: 1px solid #ddd; vertical-align: middle; }
                th { background-color: #f8f9fa; }
                .total-row { font-weight: bold; background-color: #f8f9fa; }
                .header { margin-bottom: 20px; }
                .address-section { margin: 20px 0; }
                .product-image { width: 100px; height: 100px; object-fit: cover; }
                .product-cell { display: flex; align-items: center; gap: 12px; }
            </style>
        </head>
        <body>
    """);

        // Order header
        body.append("<div class='header'>");
        body.append("<h2 class=\"text-2xl font-bold text-green-600\">Order Confirmation</h2>");
        body.append("<p>Thank you for your order! Here are your order details:</p>");
        body.append("<p>Order ID: ").append(orderDetails.getOrderId()).append("</p>");
        body.append("</div>");



        // Order items table
        body.append("<table>");
        body.append("<tr><th>Product</th><th>Size</th><th>Quantity</th><th>Price</th><th>Total</th></tr>");

        // Product details with images
        for (OrderProductDetails product : orderDetails.getOrderProducts()) {


            body.append("<tr>");
            // Product cell with image and name
            body.append("<td>");
            body.append("<div class='product-cell'>");

//            if (StringUtils.isNotBlank(imageBytes)) {
//                body.append("<img src='data:image/jpeg;base64,")
//                        .append(imageBytes.getBytes(StandardCharsets.UTF_8))
//                        .append("' class='product-image' alt='")
//                        .append("'/>");
//            }
            byte imageBytes[] = imageModelRepository.findImageByProductId(3);
            if (imageBytes != null) {
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                body.append("<img src='data:image/jpeg;base64,")
                        .append(imageBytes)
                        .append("' class='w-full h-48 object-cover'>");
            }
            body.append("<span>").append(product.getProductName()).append("</span>");
            body.append("</div>");
            body.append("</td>");

            body.append("<td>").append(product.getSize()).append("</td>");
            body.append("<td>").append(product.getQuantity()).append("</td>");
            body.append("<td>").append(formatPrice(product.getDiscountedUnitPrice())).append("</td>");

            // Calculate and append total for this item
            double itemTotal = product.getDiscountedUnitPrice() * product.getQuantity();
            body.append("<td>").append(formatPrice(itemTotal)).append("</td>");
            body.append("</tr>");
        }

        // Subtotal, shipping, and total
        body.append("<tr class='total-row'><td colspan='4'>Subtotal</td>");
        body.append("<td>").append(formatPrice(orderDetails.getOrderAmount() - orderDetails.getShippingCharge())).append("</td></tr>");

        body.append("<tr class='total-row'><td colspan='4'>Shipping</td>");
        body.append("<td>").append(formatPrice(orderDetails.getShippingCharge())).append("</td></tr>");

        body.append("<tr class='total-row'><td colspan='4'><b>Total</b></td>");
        body.append("<td><b>").append(formatPrice(orderDetails.getOrderAmount())).append("</b></td></tr>");

        body.append("</table>");

        // Footer
        body.append("""
            <div style='margin-top: 20px;'>
                <p>If you have any questions about your order, please contact our customer service.</p>
                <p>Thank you for shopping with us!</p>
            </div>
            </body>
            </html>
        """);

        return body.toString();
    }

    private String formatPrice(double price) {
        return String.format("%.2f %s", price, "INR");
    }

    private Double calculateTotalPrice(List<OrderDetails> orderDetails) {

        return orderDetails.stream()
                .mapToDouble(OrderDetails::getOrderAmount)
                .sum();

    }

    public void sendOrderConfirmationEmailHelper(String to, String subject, String body,
                                                 OrderDetails orderDetails) throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        //String fileName = "invoice_"+ orderDetails.get(0).getOrderId();
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setReplyTo("noreply@example.com");
        helper.setText(body, true);
        mimeMessage.setContent(body.toString(), "text/html; charset=utf-8");
        //byte[] pdfAttachment = pdfService.generateInvoice(orderDetails);
        // Attach the PDF file
//        helper.addAttachment(fileName, new ByteArrayResource(pdfAttachment),
//                "application/pdf");

        javaMailSender.send(mimeMessage);
    }
}
