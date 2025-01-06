package com.rapid.service;

import com.rapid.core.dto.BillingDto;
import com.rapid.core.entity.order.OrderDetails;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

@Service
public class PdfServiceImpl implements PdfService{

    //@Override
    public byte[] generateInvoice1(List<OrderDetails> orderDetails) throws IOException {
        PDDocument document = new PDDocument();

        // Create a new page
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            // Set font and font size
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

            // Add content to the PDF
            contentStream.beginText();
            contentStream.newLineAtOffset(20, 700);
            contentStream.showText("Invoice for " + orderDetails.get(0).getOrderName());
            contentStream.newLine();

            // Add order details
            for (OrderDetails order : orderDetails) {
                contentStream.showText("Item: " + order.getProduct().getProductName());
                contentStream.newLine();
                contentStream.showText("Quantity: " + order.getTotalQuantity());
                contentStream.newLine();
                contentStream.showText("Price: " + order.getTotalPrice());
                contentStream.newLine();
                contentStream.newLine();
            }

            // Add total price
            double total = calculateTotalPrice(orderDetails);
            contentStream.showText("Total Price: " + total);
            contentStream.newLine();

            contentStream.endText();
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        return outputStream.toByteArray();
    }

    public byte[] generateInvoice(List<OrderDetails> orderDetails) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;
            int rows = orderDetails.size() + 1; // Adding 1 for the header row
            float tableHeight = 20f * rows;
            float rowHeight = tableHeight / (float) rows;
            float tableBottomMargin = 70f;
            float yPositionStart = page.getMediaBox().getHeight() - margin - tableHeight - tableBottomMargin;

            drawTable(contentStream, yStart, yPositionStart, tableWidth, rowHeight, margin, orderDetails);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        return outputStream.toByteArray();
    }

    private void drawTable(PDPageContentStream contentStream, float yStart, float yPosition, float tableWidth, float rowHeight, float margin, List<OrderDetails> orderDetails) throws IOException {
        float tableYBottom = yStart - tableWidth;
        float yPositionStart = yPosition;

        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        drawHeader(contentStream, yPosition, tableWidth, rowHeight, margin);

        for (OrderDetails order : orderDetails) {
            if (yPosition <= tableYBottom) {
                drawRow(contentStream, yPosition, tableWidth, rowHeight, margin, order);
            }
            yPosition -= rowHeight;
        }

        // Draw total row
        double total = calculateTotalPrice(orderDetails);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPositionStart - rowHeight * (orderDetails.size() + 1));
        contentStream.showText("Total Price: " + formatCurrency(total));
        contentStream.endText();
    }

    private void drawHeader(PDPageContentStream contentStream, float yPosition, float tableWidth, float rowHeight, float margin) throws IOException {
        contentStream.setLineWidth(1f);
        contentStream.moveTo(margin, yPosition);
        contentStream.lineTo(margin + tableWidth, yPosition);
        contentStream.stroke();

        float nextX = margin;
        contentStream.beginText();
        contentStream.newLineAtOffset(nextX, yPosition - 15);
        contentStream.showText("Product");
        contentStream.endText();

        nextX += 150;
        contentStream.beginText();
        contentStream.newLineAtOffset(nextX, yPosition - 15);
        contentStream.showText("Quantity");
        contentStream.endText();

        nextX += 100;
        contentStream.beginText();
        contentStream.newLineAtOffset(nextX, yPosition - 15);
        contentStream.showText("Price");
        contentStream.endText();
    }

    private void drawRow(PDPageContentStream contentStream, float yPosition, float tableWidth, float rowHeight, float margin, OrderDetails order) throws IOException {
        contentStream.setLineWidth(1f);
        contentStream.moveTo(margin, yPosition);
        contentStream.lineTo(margin + tableWidth, yPosition);
        contentStream.stroke();

        float nextX = margin;
        contentStream.beginText();
        contentStream.newLineAtOffset(nextX, yPosition - 15);
        contentStream.showText(order.getProduct().getProductName());
        contentStream.endText();

        nextX += 150;
        contentStream.beginText();
        contentStream.newLineAtOffset(nextX, yPosition - 15);
        contentStream.showText(String.valueOf(order.getTotalQuantity()));
        contentStream.endText();

        nextX += 100;
        contentStream.beginText();
        contentStream.newLineAtOffset(nextX, yPosition - 15);
        contentStream.showText(formatCurrency(order.getTotalPrice()));
        contentStream.endText();
    }

    private String formatCurrency(double amount) {
        DecimalFormat df = new DecimalFormat("#,###.00");
        return "INR" + df.format(amount);
    }



    private double calculateTotalPrice(List<OrderDetails> orderDetails) {
        return orderDetails.stream().mapToDouble(OrderDetails::getTotalPrice).sum();
    }
}






