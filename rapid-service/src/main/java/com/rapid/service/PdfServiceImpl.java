package com.rapid.service;



import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.rapid.core.entity.UserAddress;
import com.rapid.core.entity.order.OrderDetails;
import com.rapid.core.entity.order.OrderProductDetails;
import com.rapid.dao.OrderRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@Service
public class PdfServiceImpl implements PdfService{

    @Autowired
    private OrderRepository orderRepository;





    //@Override
//    public void generatePDF( String orderId, String filePath) throws FileNotFoundException {
//       OrderDetails orderDetails =  orderRepository.findByOrderId(orderId);
//        PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
//        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));
//        document.add(new Paragraph("Tax Invoice").setFontSize(20).setBold());
//        document.add(new Paragraph("Order ID: " + orderDetails.getOrderId()));
//        document.add(new Paragraph("Invoice Date: " + LocalDateTime.now()));
//        document.add(new Paragraph("Order Date: " + orderDetails.getCreatedAt()));
//        document.add(new Paragraph("\nShip To:").setBold());
//        document.add(new Paragraph(orderDetails.getUserAddress().toString()));
//        document.add(new Paragraph("\nBill To:").setBold());
//        document.add(new Paragraph(orderDetails.getUserAddress().toString()));
//        document.add(new Paragraph("\nProducts:").setBold());
//        double subtotal = 0.0;
//        double discountPrice = 0.0;
//        for (OrderProductDetails product : orderDetails.getOrderProducts()) {
//            document.add(new Paragraph(
//                    product.getProductName() + " x " + product.getQuantity() + " = ₹" + product.getOriginalUnitPrice()
//            ));
//
//            subtotal+=product.getQuantity() * product.getOriginalUnitPrice();
//            discountPrice += product.getDiscountedUnitPrice();
//
//        }
//
//        document.add(new Paragraph("\nSummary:").setBold());
//        document.add(new Paragraph("Subtotal: ₹" + subtotal));
//        document.add(new Paragraph("Discount: ₹" + discountPrice));
//        document.add(new Paragraph("Shipping: ₹" + discountPrice));
//        document.add(new Paragraph("Grand Total: ₹" + orderDetails.getOrderAmount()).setBold());
//        document.add(new Paragraph("\nThank you for shopping with us!"));
//        document.close();
//    }

//    @Override
//    public byte[] generateInvoice(List<OrderDetails> orderDetails) throws IOException {
//        return new byte[0];
//    }
//
//    @Override
//    public void generatePDF(String orderId, String filePath) throws FileNotFoundException {
//        OrderDetails orderDetails = orderRepository.findByOrderId(orderId);
//
//        PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
//        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));
//
//        // Add Header
//        Paragraph header = new Paragraph("Tax Invoice")
//                .setFontSize(20)
//                .setBold()
//                .setTextAlignment(TextAlignment.CENTER);
//        document.add(header);
//
//        // Order and Invoice Details
//        Table orderDetailsTable = new Table(UnitValue.createPercentArray(new float[]{2, 5}))
//                .useAllAvailableWidth()
//                .setMarginTop(10);
//        orderDetailsTable.addCell(createCell("Order ID:", true));
//        orderDetailsTable.addCell(createCell(orderDetails.getOrderId(), false));
//        orderDetailsTable.addCell(createCell("Invoice Date:", true));
//        orderDetailsTable.addCell(createCell(LocalDateTime.now().toString(), false));
//        orderDetailsTable.addCell(createCell("Order Date:", true));
//        orderDetailsTable.addCell(createCell(orderDetails.getCreatedAt().toString(), false));
//        document.add(orderDetailsTable);
//
//        // Ship To and Bill To
//        Table addressTable = new Table(UnitValue.createPercentArray(2))
//                .useAllAvailableWidth()
//                .setMarginTop(20);
//        addressTable.addCell(createHeaderCell("Ship To:"));
//        addressTable.addCell(createHeaderCell("Bill To:"));
//        addressTable.addCell(createCell(orderDetails.getUserAddress().toString(), false));
//        addressTable.addCell(createCell(orderDetails.getUserAddress().toString(), false));
//        document.add(addressTable);
//
//        // Products Table
//        document.add(new Paragraph("\nProducts:").setBold().setFontSize(14));
//        Table productsTable = new Table(UnitValue.createPercentArray(new float[]{4, 1, 2, 2}))
//                .useAllAvailableWidth()
//                .setMarginTop(10);
//        productsTable.addHeaderCell(createHeaderCell("Product Name"));
//        productsTable.addHeaderCell(createHeaderCell("Qty"));
//        productsTable.addHeaderCell(createHeaderCell("Unit Price (₹)"));
//        productsTable.addHeaderCell(createHeaderCell("Total Price (₹)"));
//
//        double subtotal = 0.0;
//        double totalDiscount = 0.0;
//
//        for (OrderProductDetails product : orderDetails.getOrderProducts()) {
//            productsTable.addCell(createCell(product.getProductName(), false));
//            productsTable.addCell(createCell(String.valueOf(product.getQuantity()), false));
//            productsTable.addCell(createCell(String.valueOf(product.getOriginalUnitPrice()), false));
//            double productTotal = product.getQuantity() * product.getOriginalUnitPrice();
//            productsTable.addCell(createCell(String.valueOf(productTotal), false));
//            subtotal += productTotal;
//            totalDiscount += product.getQuantity() * (product.getOriginalUnitPrice() - product.getDiscountedUnitPrice());
//        }
//        document.add(productsTable);
//
//        // Summary
//        document.add(new LineSeparator(new DottedLine()).setMarginTop(10).setMarginBottom(10));
//        Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{3, 2}))
//                .useAllAvailableWidth()
//                .setMarginTop(10);
//        summaryTable.addCell(createCell("Subtotal:", true));
//        summaryTable.addCell(createCell("₹" + subtotal, false));
//        summaryTable.addCell(createCell("Discount:", true));
//        summaryTable.addCell(createCell("-₹" + totalDiscount, false));
//        summaryTable.addCell(createCell("Shipping:", true));
//        summaryTable.addCell(createCell("₹" + orderDetails.getShippingCharge(), false));
//        summaryTable.addCell(createCell("Grand Total:", true, true));
//        summaryTable.addCell(createCell("₹" + orderDetails.getOrderAmount(), false, true));
//        document.add(summaryTable);
//
//        // Footer
//        document.add(new Paragraph("\nThank you for shopping with us!")
//                .setTextAlignment(TextAlignment.CENTER)
//                .setFontSize(12)
//                .setMarginTop(20));
//
//        // Close the document
//        document.close();
//    }
//
//    private Cell createCell(String content, boolean isBold) {
//        return createCell(content, isBold, false);
//    }
//
//    private Cell createCell(String content, boolean isBold, boolean isHighlight) {
//        Cell cell = new Cell().add(new Paragraph(content));
//        if (isBold) cell.setBold();
//        if (isHighlight) cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
//        cell.setPadding(5).setBorder(Border.NO_BORDER);
//        return cell;
//    }
//
//    private Cell createHeaderCell(String content) {
//        return new Cell().add(new Paragraph(content).setBold())
//                .setPadding(5)
//                .setBackgroundColor(ColorConstants.LIGHT_GRAY);
//    }


//
//    public void generatePDF(String orderId, String filePath) throws FileNotFoundException {
//        OrderDetails orderDetails = orderRepository.findByOrderId(orderId);
//        PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
//        PdfDocument pdfDoc = new PdfDocument(writer);
//        Document document = new Document(pdfDoc, PageSize.A4);
//        document.setMargins(40, 40, 40, 40);
//
//        // Header section with company logo and invoice details
//        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}));
//        headerTable.setWidth(UnitValue.createPercentValue(100));
//
//        // Company Information (Left side)
//        Cell companyCell = new Cell();
//        companyCell.add(new Paragraph("Your Company Name")
//                .setFontSize(20)
//                .setBold()
//                .setFontColor(ColorConstants.BLUE));
//        companyCell.add(new Paragraph("123 Business Street\nCity, State 12345\nPhone: (555) 123-4567")
//                .setFontSize(10));
//        companyCell.setBorder(null);
//        headerTable.addCell(companyCell);
//
//        // Invoice Information (Right side)
//        Cell invoiceInfoCell = new Cell();
//        invoiceInfoCell.add(new Paragraph("TAX INVOICE")
//                .setFontSize(16)
//                .setBold()
//                .setTextAlignment(TextAlignment.RIGHT));
//        invoiceInfoCell.add(new Paragraph("Invoice #: " + orderDetails.getOrderId())
//                .setTextAlignment(TextAlignment.RIGHT));
//        invoiceInfoCell.add(new Paragraph("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
//                .setTextAlignment(TextAlignment.RIGHT));
//        invoiceInfoCell.setBorder(null);
//        headerTable.addCell(invoiceInfoCell);
//
//        document.add(headerTable);
//        document.add(new Paragraph("\n"));
//
//        // Billing and Shipping Information
//        Table addressTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}));
//        addressTable.setWidth(UnitValue.createPercentValue(100));
//
//        // Bill To
//        Cell billToCell = new Cell();
//        billToCell.add(new Paragraph("BILL TO").setBold());
//        billToCell.add(new Paragraph(orderDetails.getUserAddress().toString()));
//        billToCell.setBorder(null);
//        addressTable.addCell(billToCell);
//
//        // Ship To
//        Cell shipToCell = new Cell();
//        shipToCell.add(new Paragraph("SHIP TO").setBold());
//        shipToCell.add(new Paragraph(orderDetails.getUserAddress().toString()));
//        shipToCell.setBorder(null);
//        addressTable.addCell(shipToCell);
//
//        document.add(addressTable);
//        document.add(new Paragraph("\n"));
//
//        // Products Table
//        Table productTable = new Table(UnitValue.createPercentArray(new float[]{40, 15, 15, 15, 15}));
//        productTable.setWidth(UnitValue.createPercentValue(100));
//
//        // Table Headers
//        String[] headers = {"Product", "Quantity", "Unit Price", "Discount", "Total"};
//        for (String header : headers) {
//            Cell headerCell = new Cell()
//                    .add(new Paragraph(header).setBold())
//                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
//                    .setTextAlignment(TextAlignment.CENTER);
//            productTable.addHeaderCell(headerCell);
//        }
//
//        // Product Details
//        double subtotal = 0.0;
//        double totalDiscount = 0.0;
//
//        for (OrderProductDetails product : orderDetails.getOrderProducts()) {
//            double originalPrice = product.getOriginalUnitPrice();
//            double discountedPrice = product.getDiscountedUnitPrice();
//            int quantity = product.getQuantity();
//            double totalPrice = quantity * originalPrice;
//            double discount = totalPrice - (quantity * discountedPrice);
//
//            productTable.addCell(new Cell().add(new Paragraph(product.getProductName())));
//            productTable.addCell(new Cell().add(new Paragraph(String.valueOf(quantity)))
//                    .setTextAlignment(TextAlignment.CENTER));
//            productTable.addCell(new Cell().add(new Paragraph("₹" + String.format("%.2f", originalPrice)))
//                    .setTextAlignment(TextAlignment.RIGHT));
//            productTable.addCell(new Cell().add(new Paragraph("₹" + String.format("%.2f", discount)))
//                    .setTextAlignment(TextAlignment.RIGHT));
//            productTable.addCell(new Cell().add(new Paragraph("₹" + String.format("%.2f", totalPrice)))
//                    .setTextAlignment(TextAlignment.RIGHT));
//
//            subtotal += totalPrice;
//            totalDiscount += discount;
//        }
//
//        document.add(productTable);
//
//        // Summary Table
//        Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{70, 30}));
//        summaryTable.setWidth(UnitValue.createPercentValue(100));
//        summaryTable.setMarginTop(10);
//
//        // Add summary rows
//        addSummaryRow(summaryTable, "Subtotal:", String.format("₹%.2f", subtotal));
//        addSummaryRow(summaryTable, "Discount:", String.format("₹%.2f", totalDiscount));
//        addSummaryRow(summaryTable, "Shipping:", String.format("₹%.2f", orderDetails.getShippingCharge()));
//        addSummaryRow(summaryTable, "Grand Total:", String.format("₹%.2f", orderDetails.getOrderAmount()))
//                .setBold();
//
//        document.add(summaryTable);
//
//        // Footer
//        Paragraph footer = new Paragraph("\nThank you for your business!")
//                .setTextAlignment(TextAlignment.CENTER)
//                .setMarginTop(20);
//        document.add(footer);
//
//        // Terms and Conditions
//        Paragraph terms = new Paragraph("Terms and Conditions:\n" +
//                "1. Payment is due within 30 days\n" +
//                "2. Goods once sold cannot be returned\n" +
//                "3. All disputes are subject to local jurisdiction")
//                .setFontSize(8)
//                .setMarginTop(20);
//        document.add(terms);
//
//        document.close();
//    }
//
//    private Cell addSummaryRow(Table table, String label, String value) {
//        table.addCell(new Cell().add(new Paragraph(label))
//                .setTextAlignment(TextAlignment.RIGHT)
//                .setBorder(null));
//        Cell valueCell = new Cell().add(new Paragraph(value))
//                .setTextAlignment(TextAlignment.RIGHT)
//                .setBorder(null);
//        table.addCell(valueCell);
//        return valueCell;
//    }


//
//    public void generatePDF(String orderId, String filePath) throws FileNotFoundException {
//        OrderDetails orderDetails = orderRepository.findByOrderId(orderId);
//        // Set up PDF writer and document
//        PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
//        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));
//
//        // 1. Sold By Section
//        document.add(new Paragraph("Tax Invoice")
//                .setFontSize(18).setBold()
//                .setTextAlignment(TextAlignment.CENTER)
//                .setMarginBottom(20));
//
//        document.add(new Paragraph("Sold By: Tech-Connect Retail Private Limited")
//                .setBold().setFontSize(12));
//        document.add(new Paragraph("Ship-from Address: Plot no 102 part,106,107,109 and 110, Vinayak Logistic Park,")
//                .setFontSize(10));
//        document.add(new Paragraph("Village Hinaura, Hasanganj, Unnao-209859, Uttar Pradesh, Lucknow, UTTAR PRADESH, India - 209859")
//                .setFontSize(10));
//        document.add(new Paragraph("GSTIN: 09AAICA4872D1ZM")
//                .setFontSize(10).setMarginBottom(20));
//
//        // 2. Order Details Section
//        Table orderDetailsTable = new Table(UnitValue.createPercentArray(new float[]{3, 4}))
//                .useAllAvailableWidth()
//                .setMarginBottom(20);
//        orderDetailsTable.addCell(createCell("Order ID:", true));
//        orderDetailsTable.addCell(createCell(orderDetails.getOrderId(), false));
//        orderDetailsTable.addCell(createCell("Order Date:", true));
//        orderDetailsTable.addCell(createCell(orderDetails.getCreatedAt().toString(), false));
//        orderDetailsTable.addCell(createCell("Invoice Date:", true));
//        orderDetailsTable.addCell(createCell(LocalDate.now().toString(), false));
//        orderDetailsTable.addCell(createCell("PAN:", true));
//        orderDetailsTable.addCell(createCell("AAICA4872D", false));
//        orderDetailsTable.addCell(createCell("CIN:", true));
//        orderDetailsTable.addCell(createCell("U52100DL2010PTC202600", false));
//        document.add(orderDetailsTable);
//
//        // 3. Ship To and Bill To Section
//        Table addressTable = new Table(UnitValue.createPercentArray(2))
//                .useAllAvailableWidth()
//                .setMarginBottom(20);
//        addressTable.addCell(createHeaderCell("Ship To:"));
//        addressTable.addCell(createHeaderCell("Bill To:"));
//        addressTable.addCell(createCell(orderDetails.getUserAddress().toString(), false));
//        addressTable.addCell(createCell(orderDetails.getUserAddress().toString(), false));
//        document.add(addressTable);
//
//        // 4. Product Table
//        document.add(new Paragraph("Product Details")
//                .setBold().setFontSize(14).setMarginBottom(10));
//        Table productTable = new Table(UnitValue.createPercentArray(new float[]{3, 1, 2, 2, 2, 2}))
//                .useAllAvailableWidth();
//        productTable.addHeaderCell(createHeaderCell("Product Title"));
//        productTable.addHeaderCell(createHeaderCell("Qty"));
//        productTable.addHeaderCell(createHeaderCell("Gross Amount ₹"));
//        productTable.addHeaderCell(createHeaderCell("Discount ₹"));
//        productTable.addHeaderCell(createHeaderCell("Taxable Value ₹"));
//        productTable.addHeaderCell(createHeaderCell("Total ₹"));
//
//        double totalTax = 0.0, grandTotal = 0.0;
//        for (OrderProductDetails product : orderDetails.getOrderProducts()) {
//            double productTax = 100.00 * 0.18; // Example tax calculation
//            double productTotal = 200.00 + productTax;
//
//            productTable.addCell(createCell(product.getProductName(), false));
//            productTable.addCell(createCell(String.valueOf(product.getQuantity()), false));
//            productTable.addCell(createCell(String.format("₹%.2f", product.getDiscountedUnitPrice()), false));
//            productTable.addCell(createCell(String.format("-₹%.2f", product.getDiscountedUnitPrice()), false));
//            productTable.addCell(createCell(String.format("₹%.2f", product.getDiscountedUnitPrice()), false));
//            productTable.addCell(createCell(String.format("₹%.2f", productTotal), false));
//
//            totalTax += productTax;
//            grandTotal += productTotal;
//        }
//        document.add(productTable);
//
//        // 5. Summary Section
//        Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{4, 2}))
//                .useAllAvailableWidth()
//                .setMarginTop(20);
//        summaryTable.addCell(createCell("Subtotal:", true));
//        summaryTable.addCell(createCell(String.format("₹%.2f", grandTotal - totalTax), false));
//        summaryTable.addCell(createCell("Taxes (18%):", true));
//        summaryTable.addCell(createCell(String.format("₹%.2f", totalTax), false));
//        summaryTable.addCell(createCell("Grand Total:", true, true));
//        summaryTable.addCell(createCell(String.format("₹%.2f", grandTotal), false, true));
//        document.add(summaryTable);
//
//        // 6. Footer Section
//        document.add(new Paragraph("\nAuthorized Signatory")
//                .setTextAlignment(TextAlignment.RIGHT)
//                .setFontSize(12).setMarginTop(30));
//        document.add(new Paragraph("Thank you for shopping with us!")
//                .setTextAlignment(TextAlignment.CENTER)
//                .setFontSize(10).setMarginTop(20));
//        document.add(new Paragraph("For support, contact Flipkart at 1800 208 9898 or visit www.flipkart.com/helpcentre")
//                .setTextAlignment(TextAlignment.CENTER).setFontSize(8).setMarginTop(5));
//
//        // Close the document
//        document.close();
//    }
//
//    private Cell createCell(String content, boolean isBold) {
//        return createCell(content, isBold, false);
//    }
//
//    private Cell createCell(String content, boolean isBold, boolean isHighlight) {
//        Cell cell = new Cell().add(new Paragraph(content));
//        if (isBold) cell.setBold();
//        if (isHighlight) cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
//        cell.setPadding(5).setBorder(Border.NO_BORDER);
//        return cell;
//    }
//
//    private Cell createHeaderCell(String content) {
//        return new Cell().add(new Paragraph(content).setBold())
//                .setPadding(5).setBackgroundColor(ColorConstants.LIGHT_GRAY);
//    }

//    public void generatePDF(String orderId, String filePath) throws FileNotFoundException {
//        OrderDetails orderDetails = orderRepository.findByOrderId(orderId);
//        // Set up PDF writer and document
//        PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
//        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));
//
//        // 1. Sold By Section
//        document.add(new Paragraph("Tax Invoice")
//                .setFontSize(18).setBold()
//                .setTextAlignment(TextAlignment.CENTER)
//                .setMarginBottom(20));
//
//        document.add(new Paragraph("Sold By: Tech-Connect Retail Private Limited")
//                .setBold().setFontSize(12));
//        document.add(new Paragraph("Ship-from Address: Plot no 102 part,106,107,109 and 110, Vinayak Logistic Park,")
//                .setFontSize(10));
//        document.add(new Paragraph("Village Hinaura, Hasanganj, Unnao-209859, Uttar Pradesh, Lucknow, UTTAR PRADESH, India - 209859")
//                .setFontSize(10));
//        document.add(new Paragraph("GSTIN: 09AAICA4872D1ZM")
//                .setFontSize(10).setMarginBottom(20));
//
//        // 2. Order Details Section
//        Table orderDetailsTable = new Table(UnitValue.createPercentArray(new float[]{3, 4}))
//                .useAllAvailableWidth()
//                .setMarginBottom(20);
//        orderDetailsTable.addCell(createCell("Order ID:", true));
//        orderDetailsTable.addCell(createCell("OD427919512325182100", false));
//        orderDetailsTable.addCell(createCell("Order Date:", true));
//        orderDetailsTable.addCell(createCell("24-04-2023", false));
//        orderDetailsTable.addCell(createCell("Invoice Date:", true));
//        orderDetailsTable.addCell(createCell("24-04-2023", false));
//        orderDetailsTable.addCell(createCell("PAN:", true));
//        orderDetailsTable.addCell(createCell("AAICA4872D", false));
//        orderDetailsTable.addCell(createCell("CIN:", true));
//        orderDetailsTable.addCell(createCell("U52100DL2010PTC202600", false));
//        document.add(orderDetailsTable);
//
//        // 3. Ship To and Bill To Section (in a single grid)
//        Table addressTable = new Table(UnitValue.createPercentArray(new float[]{5, 5}))
//                .useAllAvailableWidth()
//                .setMarginBottom(20);
//        //addressTable.addCell(createHeaderCell("Ship To:"));
//        addressTable.addCell(createHeaderCell("Ship To:"));
//        addressTable.addCell(createHeaderCell("Bill To:"));
//        StringBuilder address = new StringBuilder();
//        UserAddress userAddress = orderDetails.getUserAddress();
//        address.append(userAddress.getName());
//        address.append("\n" + userAddress.getCity() + ", " + userAddress.getState());
//        address.append("\n" + userAddress.getPinCode());
//        address.append("\n" + userAddress.getPhoneNo());
//
//        addressTable.addCell(createCell(address.toString(), false));
//        addressTable.addCell(createCell(address.toString(), false));
//        document.add(addressTable);
//
//        // 4. Product Table (with Subtotal, Taxes, and Grand Total included)
//        document.add(new Paragraph("Product Details")
//                .setBold().setFontSize(14).setMarginBottom(10));
//        Table productTable = new Table(UnitValue.createPercentArray(new float[]{3, 1, 2, 2, 2, 2}))
//                .useAllAvailableWidth();
//        productTable.addHeaderCell(createHeaderCell("Product Title"));
//        productTable.addHeaderCell(createHeaderCell("Qty"));
//        productTable.addHeaderCell(createHeaderCell("Gross Amount ₹"));
//        productTable.addHeaderCell(createHeaderCell("Discount ₹"));
//        productTable.addHeaderCell(createHeaderCell("Taxable Value ₹"));
//        productTable.addHeaderCell(createHeaderCell("Total ₹"));
//
//        double totalTax = 0.0, grandTotal = 0.0;
//        for (OrderProductDetails product : orderDetails.getOrderProducts()) {
//            double productTax = 100.00 * 0.18; // Example tax calculation
//            double productTotal = 200.00 + productTax;
//
//            productTable.addCell(createCell(product.getProductName(), false));
//            productTable.addCell(createCell(String.valueOf(product.getQuantity()), false));
//            productTable.addCell(createCell(String.format("₹%.2f", product.getDiscountedUnitPrice()), false));
//            productTable.addCell(createCell(String.format("-₹%.2f", product.getDiscountedUnitPrice()), false));
//            productTable.addCell(createCell(String.format("₹%.2f", product.getDiscountedUnitPrice()), false));
//            productTable.addCell(createCell(String.format("₹%.2f", productTotal), false));
//
//            totalTax += productTax;
//            grandTotal += productTotal;
//        }
//
//        // Add Subtotal, Taxes, and Grand Total to the Product Table
//        productTable.addCell(createCell("Subtotal:", true));
//        productTable.addCell(createCell(String.format("₹%.2f", grandTotal - totalTax), false));
//        productTable.addCell(createCell("Taxes (18%):", true));
//        productTable.addCell(createCell(String.format("₹%.2f", totalTax), false));
//        productTable.addCell(createCell("Grand Total:", true, true));
//        productTable.addCell(createCell(String.format("₹%.2f", grandTotal), false, true));
//
//        document.add(productTable);
//
//        // 5. Footer Section
//        document.add(new Paragraph("\nAuthorized Signatory")
//                .setTextAlignment(TextAlignment.RIGHT)
//                .setFontSize(12).setMarginTop(30));
//        document.add(new Paragraph("Thank you for shopping with us!")
//                .setTextAlignment(TextAlignment.CENTER)
//                .setFontSize(10).setMarginTop(20));
//        document.add(new Paragraph("For support, contact Flipkart at 1800 208 9898 or visit www.flipkart.com/helpcentre")
//                .setTextAlignment(TextAlignment.CENTER).setFontSize(8).setMarginTop(5));
//
//        // Close the document
//        document.close();
//    }
//
//    private Cell createCell(String content, boolean isBold) {
//        return createCell(content, isBold, false);
//    }
//
//    private Cell createCell(String content, boolean isBold, boolean isHighlight) {
//        Cell cell = new Cell().add(new Paragraph(content));
//        if (isBold) cell.setBold();
//        if (isHighlight) cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
//        cell.setPadding(5).setBorder(Border.NO_BORDER);
//        return cell;
//    }
//
//    private Cell createHeaderCell(String content) {
//        return new Cell().add(new Paragraph(content).setBold())
//                .setPadding(5).setBackgroundColor(ColorConstants.LIGHT_GRAY);
//    }
//
//    public void generatePDF(String orderId, String filePath) throws FileNotFoundException {
//        OrderDetails orderDetails = orderRepository.findByOrderId(orderId);
//        // Set up PDF writer and document
//        PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
//        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));
//
//        // 1. Sold By Section
//        document.add(new Paragraph("Tax Invoice")
//                .setFontSize(18).setBold()
//                .setTextAlignment(TextAlignment.CENTER)
//                .setMarginBottom(20));
//
//        document.add(new Paragraph("Sold By: Tech-Connect Retail Private Limited")
//                .setBold().setFontSize(12));
//        document.add(new Paragraph("Ship-from Address: Plot no 102 part,106,107,109 and 110, Vinayak Logistic Park,")
//                .setFontSize(10));
//        document.add(new Paragraph("Village Hinaura, Hasanganj, Unnao-209859, Uttar Pradesh, Lucknow, UTTAR PRADESH, India - 209859")
//                .setFontSize(10));
//        document.add(new Paragraph("GSTIN: 09AAICA4872D1ZM")
//                .setFontSize(10).setMarginBottom(20));
//
//        // 2. Order Details Section
//        Table orderDetailsTable = new Table(UnitValue.createPercentArray(new float[]{3, 4}))
//                .useAllAvailableWidth()
//                .setMarginBottom(20);
//        orderDetailsTable.addCell(createCell("Order ID:", true));
//        orderDetailsTable.addCell(createCell("OD427919512325182100", false));
//        orderDetailsTable.addCell(createCell("Order Date:", true));
//        orderDetailsTable.addCell(createCell("24-04-2023", false));
//        orderDetailsTable.addCell(createCell("Invoice Date:", true));
//        orderDetailsTable.addCell(createCell("24-04-2023", false));
//        orderDetailsTable.addCell(createCell("PAN:", true));
//        orderDetailsTable.addCell(createCell("AAICA4872D", false));
//        orderDetailsTable.addCell(createCell("CIN:", true));
//        orderDetailsTable.addCell(createCell("U52100DL2010PTC202600", false));
//        document.add(orderDetailsTable);
//
//        // 3. Ship To and Bill To Section (in a single grid)
//        Table addressTable = new Table(UnitValue.createPercentArray(new float[]{5, 5}))
//                .useAllAvailableWidth()
//                .setMarginBottom(20);
//        addressTable.addCell(createHeaderCell("Ship To:"));
//        addressTable.addCell(createHeaderCell("Bill To:"));
//
//        // Address in bold and formatted
//        StringBuilder address = new StringBuilder();
//        UserAddress userAddress = orderDetails.getUserAddress();
//        address.append(userAddress.getName()).append("\n");  // Name in bold
//        address.append(userAddress.getCity()).append(", ").append(userAddress.getState()).append("\n");
//        address.append(userAddress.getPinCode()).append("\n");
//        address.append(userAddress.getPhoneNo());
//
//        addressTable.addCell(createCell(address.toString(), false));
//        addressTable.addCell(createCell(address.toString(), false));
//        document.add(addressTable);
//
//        // 4. Product Table (with Subtotal, Taxes, and Grand Total included)
//        document.add(new Paragraph("Product Details")
//                .setBold().setFontSize(14).setMarginBottom(10));
//        Table productTable = new Table(UnitValue.createPercentArray(new float[]{3, 1, 2, 2, 2, 2}))
//                .useAllAvailableWidth();
//        productTable.addHeaderCell(createHeaderCell("Product Title"));
//        productTable.addHeaderCell(createHeaderCell("Qty"));
//        productTable.addHeaderCell(createHeaderCell("Gross Amount ₹"));
//        productTable.addHeaderCell(createHeaderCell("Discount ₹"));
//        productTable.addHeaderCell(createHeaderCell("Taxable Value ₹"));
//        productTable.addHeaderCell(createHeaderCell("Total ₹"));
//
//        double totalTax = 0.0, grandTotal = 0.0;
//        for (OrderProductDetails product : orderDetails.getOrderProducts()) {
//            double productTax = 100.00 * 0.18; // Example tax calculation
//            double productTotal = 200.00 + productTax;
//
//            // Check if company name is "Rapid Groove" and make it bold
//            String productName = product.getProductName();
//            if ("Rapid Groove".equalsIgnoreCase(productName)) {
//                productName = new Text(productName).setBold().toString();
//            }
//
//            productTable.addCell(createCell(productName, false));
//            productTable.addCell(createCell(String.valueOf(product.getQuantity()), false));
//            productTable.addCell(createCell(String.format("₹%.2f", product.getDiscountedUnitPrice()), false));
//            productTable.addCell(createCell(String.format("-₹%.2f", product.getDiscountedUnitPrice()), false));
//            productTable.addCell(createCell(String.format("₹%.2f", product.getDiscountedUnitPrice()), false));
//            productTable.addCell(createCell(String.format("₹%.2f", productTotal), false));
//
//            totalTax += productTax;
//            grandTotal += productTotal;
//        }
//
//        // Add Subtotal, Taxes, and Grand Total to the Product Table in the last row
//        productTable.addCell(createCell("Subtotal:", true));
//        productTable.addCell(createCell(String.format("₹%.2f", grandTotal - totalTax), false));
//        productTable.addCell(createCell("Taxes (18%):", true));
//        productTable.addCell(createCell(String.format("₹%.2f", totalTax), false));
//        productTable.addCell(createCell("Grand Total:", true, true));
//        productTable.addCell(createCell(String.format("₹%.2f", grandTotal), false, true));
//
//        document.add(productTable);
//
//        // 5. Footer Section
//        document.add(new Paragraph("\nAuthorized Signatory")
//                .setTextAlignment(TextAlignment.RIGHT)
//                .setFontSize(12).setMarginTop(30));
//        document.add(new Paragraph("Thank you for shopping with us!")
//                .setTextAlignment(TextAlignment.CENTER)
//                .setFontSize(10).setMarginTop(20));
//        document.add(new Paragraph("For support, contact Flipkart at 1800 208 9898 or visit www.flipkart.com/helpcentre")
//                .setTextAlignment(TextAlignment.CENTER).setFontSize(8).setMarginTop(5));
//
//        // Close the document
//        document.close();
//    }
//
//    private Cell createCell(String content, boolean isBold) {
//        return createCell(content, isBold, false);
//    }
//
//    private Cell createCell(String content, boolean isBold, boolean isHighlight) {
//        Cell cell = new Cell().add(new Paragraph(content));
//        if (isBold) cell.setBold();
//        if (isHighlight) cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
//        cell.setPadding(5).setBorder(Border.NO_BORDER);
//        return cell;
//    }
//
//    private Cell createHeaderCell(String content) {
//        return new Cell().add(new Paragraph(content).setBold())
//                .setPadding(5).setBackgroundColor(ColorConstants.LIGHT_GRAY);
//    }

//
//    public void generatePDF(String orderId, String filePath) throws FileNotFoundException {
//        OrderDetails orderDetails = orderRepository.findByOrderId(orderId);
//        PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
//        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));
//
//        // Company Name at Top Left
//        document.add(new Paragraph("Rapid Groove")
//                .setFontSize(24)
//                .setBold()
//                .setTextAlignment(TextAlignment.LEFT)
//                .setMarginBottom(10));
//
//        // Tax Invoice Title
//        document.add(new Paragraph("Tax Invoice")
//                .setFontSize(18)
//                .setBold()
//                .setTextAlignment(TextAlignment.CENTER)
//                .setMarginBottom(20));
//
//        // Sold By Section
//        document.add(new Paragraph("Sold By: Tech-Connect Retail Private Limited")
//                .setBold().setFontSize(12));
//        document.add(new Paragraph("Ship-from Address: Plot no 102 part,106,107,109 and 110, Vinayak Logistic Park,")
//                .setFontSize(10));
//        document.add(new Paragraph("Village Hinaura, Hasanganj, Unnao-209859, Uttar Pradesh, Lucknow, UTTAR PRADESH, India - 209859")
//                .setFontSize(10));
//        document.add(new Paragraph("GSTIN: 09AAICA4872D1ZM")
//                .setFontSize(10).setMarginBottom(20));
//
//        // Order Details Section
//        Table orderDetailsTable = new Table(UnitValue.createPercentArray(new float[]{3, 4}))
//                .useAllAvailableWidth()
//                .setMarginBottom(20);
//        orderDetailsTable.addCell(createCell("Order ID:", true));
//        orderDetailsTable.addCell(createCell("OD427919512325182100", false));
//        orderDetailsTable.addCell(createCell("Order Date:", true));
//        orderDetailsTable.addCell(createCell("24-04-2023", false));
//        orderDetailsTable.addCell(createCell("Invoice Date:", true));
//        orderDetailsTable.addCell(createCell("24-04-2023", false));
//        orderDetailsTable.addCell(createCell("PAN:", true));
//        orderDetailsTable.addCell(createCell("AAICA4872D", false));
//        orderDetailsTable.addCell(createCell("CIN:", true));
//        orderDetailsTable.addCell(createCell("U52100DL2010PTC202600", false));
//        document.add(orderDetailsTable);
//
//        // Ship To and Bill To Section
//        Table addressTable = new Table(UnitValue.createPercentArray(new float[]{5, 5}))
//                .useAllAvailableWidth()
//                .setMarginBottom(20);
//        addressTable.addCell(createHeaderCell("Ship To:"));
//        addressTable.addCell(createHeaderCell("Bill To:"));
//
//        StringBuilder address = new StringBuilder();
//        UserAddress userAddress = orderDetails.getUserAddress();
//        address.append(userAddress.getName()).append("\n");
//        address.append(userAddress.getCity()).append(", ").append(userAddress.getState()).append("\n");
//        address.append(userAddress.getPinCode()).append("\n");
//        address.append(userAddress.getPhoneNo());
//
//        addressTable.addCell(createCell(address.toString(), false));
//        addressTable.addCell(createCell(address.toString(), false));
//        document.add(addressTable);
//
//        // Product Details Section with Fixed Amount
//        document.add(new Paragraph("Product Details")
//                .setBold().setFontSize(14).setMarginBottom(10));
//        Table productTable = new Table(UnitValue.createPercentArray(new float[]{3, 1, 2, 2, 2, 2}))
//                .useAllAvailableWidth();
//        productTable.addHeaderCell(createHeaderCell("Product Title"));
//        productTable.addHeaderCell(createHeaderCell("Qty"));
//        productTable.addHeaderCell(createHeaderCell("Gross Amount ₹"));
//        productTable.addHeaderCell(createHeaderCell("Discount ₹"));
//        productTable.addHeaderCell(createHeaderCell("Taxable Value ₹"));
//        productTable.addHeaderCell(createHeaderCell("Total ₹"));
//
//        // Add product rows from orderDetails
//        for (OrderProductDetails product : orderDetails.getOrderProducts()) {
//            productTable.addCell(createCell(product.getProductName(), false));
//            productTable.addCell(createCell(String.valueOf(product.getQuantity()), false));
//            productTable.addCell(createCell(String.format("₹%.2f", product.getDiscountedUnitPrice()), false));
//            productTable.addCell(createCell(String.format("-₹%.2f", product.getDiscountedUnitPrice()), false));
//            productTable.addCell(createCell(String.format("₹%.2f", product.getDiscountedUnitPrice()), false));
//            productTable.addCell(createCell("₹200.00", false));
//        }
//
//        // Fixed amounts in Product Details section
//        productTable.addCell(createCell("Subtotal:", true));
//        productTable.addCell(createCell("", false));
//        productTable.addCell(createCell("", false));
//        productTable.addCell(createCell("", false));
//        productTable.addCell(createCell("", false));
//        productTable.addCell(createCell("₹200.00", false));
//
//        productTable.addCell(createCell("Taxes (18%):", true));
//        productTable.addCell(createCell("", false));
//        productTable.addCell(createCell("", false));
//        productTable.addCell(createCell("", false));
//        productTable.addCell(createCell("", false));
//        productTable.addCell(createCell("₹36.00", false));
//
//        productTable.addCell(createCell("Grand Total:", true, true));
//        productTable.addCell(createCell("", false));
//        productTable.addCell(createCell("", false));
//        productTable.addCell(createCell("", false));
//        productTable.addCell(createCell("", false));
//        productTable.addCell(createCell("₹236.00", false, true));
//
//        document.add(productTable);
//
//        // Amount in Words
//        document.add(new Paragraph("Amount in Words: Rupees Two Hundred Thirty Six Only")
//                .setFontSize(10)
//                .setMarginTop(10)
//                .setMarginBottom(20));
//
//        // Footer Section
//        document.add(new Paragraph("\nAuthorized Signatory")
//                .setTextAlignment(TextAlignment.RIGHT)
//                .setFontSize(12).setMarginTop(30));
//        document.add(new Paragraph("Thank you for shopping with us!")
//                .setTextAlignment(TextAlignment.CENTER)
//                .setFontSize(10).setMarginTop(20));
//        document.add(new Paragraph("For support, contact Flipkart at 1800 208 9898 or visit www.flipkart.com/helpcentre")
//                .setTextAlignment(TextAlignment.CENTER)
//                .setFontSize(8)
//                .setMarginTop(5));
//
//        document.close();
//    }
//
//    private Cell createCell(String content, boolean isBold) {
//        return createCell(content, isBold, false);
//    }
//
//    private Cell createCell(String content, boolean isBold, boolean isHighlight) {
//        Cell cell = new Cell().add(new Paragraph(content));
//        if (isBold) cell.setBold();
//        if (isHighlight) cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
//        cell.setPadding(5).setBorder(Border.NO_BORDER);
//        return cell;
//    }
//
//    private Cell createHeaderCell(String content) {
//        return new Cell().add(new Paragraph(content).setBold())
//                .setPadding(5)
//                .setBackgroundColor(ColorConstants.LIGHT_GRAY);
//    }
//
//
//
//


//    public void generatePDF(String orderId, String filePath) throws FileNotFoundException {
//        OrderDetails orderDetails = orderRepository.findByOrderId(orderId);
//        PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
//        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));
//
//        // Set page margins
//        document.setMargins(40, 40, 40, 40);
//
//        // Header Section with Modern Layout
//        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
//                .useAllAvailableWidth()
//                .setBorder(Border.NO_BORDER);
//
//        // Left side - Company Info
//        Cell leftHeaderCell = new Cell();
//        leftHeaderCell.add(new Paragraph("Rapid Groove")
//                .setFontSize(28)
//                .setBold()
//                .setFontColor(new DeviceRgb(51, 51, 51)));
//        leftHeaderCell.add(new Paragraph("Tech-Connect Retail Private Limited")
//                .setFontSize(12)
//                .setFontColor(new DeviceRgb(102, 102, 102)));
//        leftHeaderCell.add(new Paragraph("GSTIN: 09AAICA4872D1ZM")
//                .setFontSize(10)
//                .setFontColor(new DeviceRgb(102, 102, 102)));
//        leftHeaderCell.setBorder(Border.NO_BORDER);
//        headerTable.addCell(leftHeaderCell);
//
//        // Right side - Invoice Title and Number
//        Cell rightHeaderCell = new Cell();
//        rightHeaderCell.add(new Paragraph("TAX INVOICE")
//                .setFontSize(24)
//                .setBold()
//                .setTextAlignment(TextAlignment.RIGHT)
//                .setFontColor(new DeviceRgb(51, 51, 51)));
//        rightHeaderCell.add(new Paragraph("Invoice #: " + orderId)
//                .setFontSize(12)
//                .setTextAlignment(TextAlignment.RIGHT)
//                .setFontColor(new DeviceRgb(102, 102, 102)));
//        rightHeaderCell.setBorder(Border.NO_BORDER);
//        headerTable.addCell(rightHeaderCell);
//
//        document.add(headerTable);
//
//        // Add separator line
//        document.add(new LineSeparator(new SolidLine(1))
//                .setMarginTop(20)
//                .setMarginBottom(20));
//
//        // Address and Details Section
//        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
//                .useAllAvailableWidth()
//                .setMarginBottom(20);
//
//        // Bill To Section
//        Cell billToCell = new Cell();
//        billToCell.add(new Paragraph("BILL TO")
//                .setBold()
//                .setFontSize(12)
//                .setFontColor(new DeviceRgb(51, 51, 51)));
//        UserAddress userAddress = orderDetails.getUserAddress();
//        billToCell.add(new Paragraph(userAddress.getName())
//                .setFontSize(11));
//        billToCell.add(new Paragraph(userAddress.getCity() + ", " + userAddress.getState())
//                .setFontSize(11));
//        billToCell.add(new Paragraph(userAddress.getPinCode())
//                .setFontSize(11));
//        billToCell.add(new Paragraph(userAddress.getPhoneNo())
//                .setFontSize(11));
//        billToCell.setBorder(Border.NO_BORDER);
//        infoTable.addCell(billToCell);
//
//        // Invoice Details Section
//        Cell invoiceDetailsCell = new Cell();
//        invoiceDetailsCell.add(new Paragraph("INVOICE DETAILS")
//                .setBold()
//                .setFontSize(12)
//                .setFontColor(new DeviceRgb(51, 51, 51)));
//        invoiceDetailsCell.add(new Paragraph("Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")))
//                .setFontSize(11));
//        invoiceDetailsCell.add(new Paragraph("PAN: AAICA4872D")
//                .setFontSize(11));
//        invoiceDetailsCell.add(new Paragraph("CIN: U52100DL2010PTC202600")
//                .setFontSize(11));
//        invoiceDetailsCell.setBorder(Border.NO_BORDER);
//        infoTable.addCell(invoiceDetailsCell);
//
//        document.add(infoTable);
//
//        // Products Table with Modern Styling
//        Table productTable = new Table(UnitValue.createPercentArray(new float[]{4, 1, 2, 2, 2}))
//                .useAllAvailableWidth()
//                .setMarginTop(20);
//
//        // Table Headers
//        Stream.of("Product Description", "Qty", "Price", "Tax (18%)", "Total")
//                .forEach(title -> {
//                    Cell header = new Cell().add(new Paragraph(title)
//                            .setBold()
//                            .setFontSize(11)
//                            .setFontColor(new DeviceRgb(255, 255, 255)));
//                    header.setBackgroundColor(new DeviceRgb(51, 51, 51));
//                    header.setPadding(10);
//                    productTable.addHeaderCell(header);
//                });
//
//        // Add products
//        for (OrderProductDetails product : orderDetails.getOrderProducts()) {
//            productTable.addCell(createStyledCell(product.getProductName()));
//            productTable.addCell(createStyledCell(String.valueOf(product.getQuantity())));
//            productTable.addCell(createStyledCell("₹200.00"));
//            productTable.addCell(createStyledCell("₹36.00"));
//            productTable.addCell(createStyledCell("₹236.00"));
//        }
//
//        document.add(productTable);
//
//        // Totals Section with Right Alignment
//        Table totalsTable = new Table(UnitValue.createPercentArray(new float[]{7, 3}))
//                .useAllAvailableWidth()
//                .setMarginTop(20);
//
//        // Add totals rows
//        addTotalRow(totalsTable, "Subtotal:", "₹200.00", false);
//        addTotalRow(totalsTable, "Tax (18%):", "₹36.00", false);
//        addTotalRow(totalsTable, "Grand Total:", "₹236.00", true);
//
//        document.add(totalsTable);
//
//        // Amount in Words
//        document.add(new Paragraph("Amount in Words: Rupees Two Hundred Thirty Six Only")
//                .setFontSize(11)
//                .setFontColor(new DeviceRgb(102, 102, 102))
//                .setMarginTop(20));
//
//        // Footer
//        Table footerTable = new Table(UnitValue.createPercentArray(new float[]{2, 1}))
//                .useAllAvailableWidth()
//                .setMarginTop(40);
//
//        // Terms and Conditions
//        Cell termsCell = new Cell();
//        termsCell.add(new Paragraph("Terms & Conditions")
//                .setBold()
//                .setFontSize(11)
//                .setFontColor(new DeviceRgb(51, 51, 51)));
//        termsCell.add(new Paragraph("Payment is due within 15 days\nPlease make checks payable to Rapid Groove")
//                .setFontSize(10)
//                .setFontColor(new DeviceRgb(102, 102, 102)));
//        termsCell.setBorder(Border.NO_BORDER);
//        footerTable.addCell(termsCell);
//
//        // Signature
//        Cell signatureCell = new Cell();
//        signatureCell.add(new Paragraph("Authorized Signatory")
//                .setBold()
//                .setFontSize(11)
//                .setTextAlignment(TextAlignment.RIGHT)
//                .setFontColor(new DeviceRgb(51, 51, 51)));
//        signatureCell.setBorder(Border.NO_BORDER);
//        footerTable.addCell(signatureCell);
//
//        document.add(footerTable);
//
//        document.close();
//    }
//
//    private Cell createStyledCell(String content) {
//        Cell cell = new Cell().add(new Paragraph(content).setFontSize(10));
//        cell.setPadding(8);
//        cell.setFontColor(new DeviceRgb(51, 51, 51));
//        return cell;
//    }
//
//    private void addTotalRow(Table table, String label, String amount, boolean isFinal) {
//        Cell labelCell = new Cell()
//                .add(new Paragraph(label)
//                        .setTextAlignment(TextAlignment.RIGHT)
//                        .setFontSize(11)
//                        .setBold());
//        labelCell.setBorder(Border.NO_BORDER);
//
//        Cell amountCell = new Cell()
//                .add(new Paragraph(amount)
//                        .setTextAlignment(TextAlignment.RIGHT)
//                        .setFontSize(11)
//                        .setBold());
//        if (isFinal) {
//            amountCell.setBackgroundColor(new DeviceRgb(51, 51, 51));
//            amountCell.setFontColor(new DeviceRgb(255, 255, 255));
//        }
//        amountCell.setBorder(Border.NO_BORDER);
//
//        table.addCell(labelCell);
//        table.addCell(amountCell);
//    }

//
//    public void generatePDF(String orderId, String filePath) throws FileNotFoundException {
//        OrderDetails orderDetails = orderRepository.findByOrderId(orderId);
//        PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
//        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));
//
//        // Set page margins
//        document.setMargins(40, 40, 40, 40);
//
//        // Header Section with Modern Layout
//        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
//                .useAllAvailableWidth()
//                .setBorder(Border.NO_BORDER);
//
//        // Left side - Company Info
//        Cell leftHeaderCell = new Cell();
//        leftHeaderCell.add(new Paragraph("Rapid Groove")
//                .setFontSize(28)
//                .setBold()
//                .setFontColor(new DeviceRgb(51, 51, 51)));
//        leftHeaderCell.add(new Paragraph("Tech-Connect Retail Private Limited")
//                .setFontSize(12)
//                .setFontColor(new DeviceRgb(102, 102, 102)));
//        leftHeaderCell.add(new Paragraph("GSTIN: 09AAICA4872D1ZM")
//                .setFontSize(10)
//                .setFontColor(new DeviceRgb(102, 102, 102)));
//        leftHeaderCell.setBorder(Border.NO_BORDER);
//        headerTable.addCell(leftHeaderCell);
//
//        // Right side - Invoice Title and Number
//        Cell rightHeaderCell = new Cell();
//        rightHeaderCell.add(new Paragraph("TAX INVOICE")
//                .setFontSize(24)
//                .setBold()
//                .setTextAlignment(TextAlignment.RIGHT)
//                .setFontColor(new DeviceRgb(51, 51, 51)));
//        rightHeaderCell.add(new Paragraph("Invoice #: " + orderId)
//                .setFontSize(12)
//                .setTextAlignment(TextAlignment.RIGHT)
//                .setFontColor(new DeviceRgb(102, 102, 102)));
//        rightHeaderCell.setBorder(Border.NO_BORDER);
//        headerTable.addCell(rightHeaderCell);
//
//        document.add(headerTable);
//
//        // Add separator line
//        document.add(new LineSeparator(new SolidLine(1))
//                .setMarginTop(20)
//                .setMarginBottom(20));
//
//        // Address and Details Section - Now with 3 columns
//        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}))
//                .useAllAvailableWidth()
//                .setMarginBottom(20);
//
//        // Bill To Section
//        Cell billToCell = new Cell();
//        billToCell.add(new Paragraph("BILL TO")
//                .setBold()
//                .setFontSize(12)
//                .setFontColor(new DeviceRgb(51, 51, 51)));
//        UserAddress billingAddress = orderDetails.getUserAddress(); // Assuming this is billing address
//        billToCell.add(new Paragraph(billingAddress.getName())
//                .setFontSize(11));
//        billToCell.add(new Paragraph(billingAddress.getCity() + ", " + billingAddress.getState())
//                .setFontSize(11));
//        billToCell.add(new Paragraph(billingAddress.getPinCode())
//                .setFontSize(11));
//        billToCell.add(new Paragraph(billingAddress.getPhoneNo())
//                .setFontSize(11));
//        billToCell.setBorder(Border.NO_BORDER);
//        infoTable.addCell(billToCell);
//
//        // Ship To Section
//        Cell shipToCell = new Cell();
//        shipToCell.add(new Paragraph("SHIP TO")
//                .setBold()
//                .setFontSize(12)
//                .setFontColor(new DeviceRgb(51, 51, 51)));
//        UserAddress shippingAddress = orderDetails.getUserAddress(); // Assuming you have shipping address
//        shipToCell.add(new Paragraph(shippingAddress.getName())
//                .setFontSize(11));
//        shipToCell.add(new Paragraph(shippingAddress.getCity() + ", " + shippingAddress.getState())
//                .setFontSize(11));
//        shipToCell.add(new Paragraph(shippingAddress.getPinCode())
//                .setFontSize(11));
//        shipToCell.add(new Paragraph(shippingAddress.getPhoneNo())
//                .setFontSize(11));
//        shipToCell.setBorder(Border.NO_BORDER);
//        infoTable.addCell(shipToCell);
//
//        // Invoice Details Section
//        Cell invoiceDetailsCell = new Cell();
//        invoiceDetailsCell.add(new Paragraph("INVOICE DETAILS")
//                .setBold()
//                .setFontSize(12)
//                .setFontColor(new DeviceRgb(51, 51, 51)));
//        invoiceDetailsCell.add(new Paragraph("Invoice Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")))
//                .setFontSize(11));
//        invoiceDetailsCell.add(new Paragraph("Order Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")))
//                .setFontSize(11));
//        invoiceDetailsCell.add(new Paragraph("PAN: AAICA4872D")
//                .setFontSize(11));
//        invoiceDetailsCell.add(new Paragraph("CIN: U52100DL2010PTC202600")
//                .setFontSize(11));
//        invoiceDetailsCell.setBorder(Border.NO_BORDER);
//        infoTable.addCell(invoiceDetailsCell);
//
//        document.add(infoTable);
//
//        // Products Table with Modern Styling
//        Table productTable = new Table(UnitValue.createPercentArray(new float[]{4, 1, 2, 2, 2}))
//                .useAllAvailableWidth()
//                .setMarginTop(20);
//
//        // Table Headers
//        Stream.of("Product Description", "Qty", "Price", "Tax (18%)", "Total")
//                .forEach(title -> {
//                    Cell header = new Cell().add(new Paragraph(title)
//                            .setBold()
//                            .setFontSize(11)
//                            .setFontColor(new DeviceRgb(255, 255, 255)));
//                    header.setBackgroundColor(new DeviceRgb(51, 51, 51));
//                    header.setPadding(10);
//                    productTable.addHeaderCell(header);
//                });
//
//        // Add products
//        for (OrderProductDetails product : orderDetails.getOrderProducts()) {
//            productTable.addCell(createStyledCell(product.getProductName()));
//            productTable.addCell(createStyledCell(String.valueOf(product.getQuantity())));
//            productTable.addCell(createStyledCell("₹200.00"));
//            productTable.addCell(createStyledCell("₹36.00"));
//            productTable.addCell(createStyledCell("₹236.00"));
//        }
//
//        document.add(productTable);
//
//        // Totals Section with Right Alignment
//        Table totalsTable = new Table(UnitValue.createPercentArray(new float[]{7, 3}))
//                .useAllAvailableWidth()
//                .setMarginTop(20);
//
//        // Add totals rows
//        addTotalRow(totalsTable, "Subtotal:", "₹200.00", false);
//        addTotalRow(totalsTable, "Tax (18%):", "₹36.00", false);
//        addTotalRow(totalsTable, "Grand Total:", "₹236.00", true);
//
//        document.add(totalsTable);
//
//        // Amount in Words
//        document.add(new Paragraph("Amount in Words: Rupees Two Hundred Thirty Six Only")
//                .setFontSize(11)
//                .setFontColor(new DeviceRgb(102, 102, 102))
//                .setMarginTop(20));
//
//        // Footer
//        Table footerTable = new Table(UnitValue.createPercentArray(new float[]{2, 1}))
//                .useAllAvailableWidth()
//                .setMarginTop(40);
//
//        // Terms and Conditions
//        Cell termsCell = new Cell();
//        termsCell.add(new Paragraph("Terms & Conditions")
//                .setBold()
//                .setFontSize(11)
//                .setFontColor(new DeviceRgb(51, 51, 51)));
//        termsCell.add(new Paragraph("Payment is due within 15 days\nPlease make checks payable to Rapid Groove")
//                .setFontSize(10)
//                .setFontColor(new DeviceRgb(102, 102, 102)));
//        termsCell.setBorder(Border.NO_BORDER);
//        footerTable.addCell(termsCell);
//
//        // Signature
//        Cell signatureCell = new Cell();
//        signatureCell.add(new Paragraph("Authorized Signatory")
//                .setBold()
//                .setFontSize(11)
//                .setTextAlignment(TextAlignment.RIGHT)
//                .setFontColor(new DeviceRgb(51, 51, 51)));
//        signatureCell.setBorder(Border.NO_BORDER);
//        footerTable.addCell(signatureCell);
//
//        document.add(footerTable);
//
//        document.close();
//    }
//
//    // Helper methods remain the same
//    private Cell createStyledCell(String content) {
//        Cell cell = new Cell().add(new Paragraph(content).setFontSize(10));
//        cell.setPadding(8);
//        cell.setFontColor(new DeviceRgb(51, 51, 51));
//        return cell;
//    }
//
//    private void addTotalRow(Table table, String label, String amount, boolean isFinal) {
//        Cell labelCell = new Cell()
//                .add(new Paragraph(label)
//                        .setTextAlignment(TextAlignment.RIGHT)
//                        .setFontSize(11)
//                        .setBold());
//        labelCell.setBorder(Border.NO_BORDER);
//
//        Cell amountCell = new Cell()
//                .add(new Paragraph(amount)
//                        .setTextAlignment(TextAlignment.RIGHT)
//                        .setFontSize(11)
//                        .setBold());
//        if (isFinal) {
//            amountCell.setBackgroundColor(new DeviceRgb(51, 51, 51));
//            amountCell.setFontColor(new DeviceRgb(255, 255, 255));
//        }
//        amountCell.setBorder(Border.NO_BORDER);
//
//        table.addCell(labelCell);
//        table.addCell(amountCell);
//    }

//
//    public void generatePDF(String orderId, String filePath) throws FileNotFoundException {
//        OrderDetails orderDetails = orderRepository.findByOrderId(orderId);
//        PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
//        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));
//
//        // Set page margins for better spacing
//        document.setMargins(50, 50, 50, 50);
//
//        // Header Section
//        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
//                .useAllAvailableWidth()
//                .setBorder(Border.NO_BORDER);
//
//        // Left side - Company Info
//        Cell leftHeaderCell = new Cell();
//        leftHeaderCell.add(new Paragraph("Rapid Groove")
//                .setFontSize(32)  // Increased for better prominence
//                .setBold()
//                .setFontColor(new DeviceRgb(51, 51, 51)));
//        leftHeaderCell.add(new Paragraph("Tech-Connect Retail Private Limited")
//                .setFontSize(14)  // Increased for better readability
//                .setFontColor(new DeviceRgb(102, 102, 102))
//                .setMarginTop(5));
//        leftHeaderCell.add(new Paragraph("GSTIN: 09AAICA4872D1ZM")
//                .setFontSize(12)  // Adjusted for hierarchy
//                .setFontColor(new DeviceRgb(102, 102, 102))
//                .setMarginTop(2));
//        leftHeaderCell.setBorder(Border.NO_BORDER);
//        headerTable.addCell(leftHeaderCell);
//
//        // Right side - Invoice Title and Number
//        Cell rightHeaderCell = new Cell();
//        rightHeaderCell.add(new Paragraph("TAX INVOICE")
//                .setFontSize(26)  // Adjusted for balance with company name
//                .setBold()
//                .setTextAlignment(TextAlignment.RIGHT)
//                .setFontColor(new DeviceRgb(51, 51, 51)));
//        rightHeaderCell.add(new Paragraph("Invoice #: " + orderId)
//                .setFontSize(14)  // Increased for better visibility
//                .setTextAlignment(TextAlignment.RIGHT)
//                .setFontColor(new DeviceRgb(102, 102, 102))
//                .setMarginTop(5));
//        rightHeaderCell.setBorder(Border.NO_BORDER);
//        headerTable.addCell(rightHeaderCell);
//
//        document.add(headerTable);
//
//        // Separator line
//        document.add(new LineSeparator(new SolidLine(1))
//                .setMarginTop(30)
//                .setMarginBottom(30));
//
//        // Address and Details Section
//        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}))
//                .useAllAvailableWidth()
//                .setMarginBottom(30);
//
//        // Bill To Section
//        Cell billToCell = new Cell();
//        billToCell.add(new Paragraph("BILL TO")
//                .setBold()
//                .setFontSize(14)  // Increased section header
//                .setFontColor(new DeviceRgb(51, 51, 51))
//                .setMarginBottom(10));
//        UserAddress billingAddress = orderDetails.getUserAddress();
//        billToCell.add(new Paragraph(billingAddress.getName())
//                .setFontSize(13)  // Increased name size
//                .setBold());
//        billToCell.add(new Paragraph(billingAddress.getCity() + ", " + billingAddress.getState())
//                .setFontSize(12)
//                .setMarginTop(5));
//        billToCell.add(new Paragraph(billingAddress.getPinCode())
//                .setFontSize(12));
//        billToCell.add(new Paragraph(billingAddress.getPhoneNo())
//                .setFontSize(12)
//                .setMarginTop(5));
//        billToCell.setBorder(Border.NO_BORDER);
//        infoTable.addCell(billToCell);
//
//        // Ship To Section
//        Cell shipToCell = new Cell();
//        shipToCell.add(new Paragraph("SHIP TO")
//                .setBold()
//                .setFontSize(14)  // Increased section header
//                .setFontColor(new DeviceRgb(51, 51, 51))
//                .setMarginBottom(10));
//        UserAddress shippingAddress = orderDetails.getUserAddress();;
//        shipToCell.add(new Paragraph(shippingAddress.getName())
//                .setFontSize(13)  // Increased name size
//                .setBold());
//        shipToCell.add(new Paragraph(shippingAddress.getCity() + ", " + shippingAddress.getState())
//                .setFontSize(12)
//                .setMarginTop(5));
//        shipToCell.add(new Paragraph(shippingAddress.getPinCode())
//                .setFontSize(12));
//        shipToCell.add(new Paragraph(shippingAddress.getPhoneNo())
//                .setFontSize(12)
//                .setMarginTop(5));
//        shipToCell.setBorder(Border.NO_BORDER);
//        infoTable.addCell(shipToCell);
//
//        // Invoice Details Section
//        Cell invoiceDetailsCell = new Cell();
//        invoiceDetailsCell.add(new Paragraph("INVOICE DETAILS")
//                .setBold()
//                .setFontSize(14)  // Increased section header
//                .setFontColor(new DeviceRgb(51, 51, 51))
//                .setMarginBottom(10));
//        invoiceDetailsCell.add(new Paragraph("Invoice Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")))
//                .setFontSize(12)
//                .setMarginTop(5));
//        invoiceDetailsCell.add(new Paragraph("Order Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")))
//                .setFontSize(12)
//                .setMarginTop(5));
//        invoiceDetailsCell.add(new Paragraph("PAN: AAICA4872D")
//                .setFontSize(12)
//                .setMarginTop(5));
//        invoiceDetailsCell.add(new Paragraph("CIN: U52100DL2010PTC202600")
//                .setFontSize(12)
//                .setMarginTop(5));
//        invoiceDetailsCell.setBorder(Border.NO_BORDER);
//        infoTable.addCell(invoiceDetailsCell);
//
//        document.add(infoTable);
//
//        // Products Table
//        Table productTable = new Table(UnitValue.createPercentArray(new float[]{4, 1, 2, 2, 2}))
//                .useAllAvailableWidth()
//                .setMarginTop(20);
//
//        // Table Headers
//        Stream.of("Product Description", "Qty", "Price", "Tax (18%)", "Total")
//                .forEach(title -> {
//                    Cell header = new Cell().add(new Paragraph(title)
//                            .setBold()
//                            .setFontSize(13)  // Increased header size
//                            .setFontColor(new DeviceRgb(255, 255, 255)));
//                    header.setBackgroundColor(new DeviceRgb(51, 51, 51));
//                    header.setPadding(12);  // Increased padding
//                    productTable.addHeaderCell(header);
//                });
//
//        // Add products with larger font
//        for (OrderProductDetails product : orderDetails.getOrderProducts()) {
//            productTable.addCell(createStyledCell(product.getProductName(), 12));  // Increased size
//            productTable.addCell(createStyledCell(String.valueOf(product.getQuantity()), 12));
//            productTable.addCell(createStyledCell("₹200.00", 12));
//            productTable.addCell(createStyledCell("₹36.00", 12));
//            productTable.addCell(createStyledCell("₹236.00", 12));
//        }
//
//        document.add(productTable);
//
//        // Totals Section
//        Table totalsTable = new Table(UnitValue.createPercentArray(new float[]{7, 3}))
//                .useAllAvailableWidth()
//                .setMarginTop(30);
//
//        addTotalRow(totalsTable, "Subtotal:", "₹200.00", false, 13);  // Increased size
//        addTotalRow(totalsTable, "Tax (18%):", "₹36.00", false, 13);
//        addTotalRow(totalsTable, "Grand Total:", "₹236.00", true, 14);  // Made largest
//
//        document.add(totalsTable);
//
//        // Amount in Words
//        document.add(new Paragraph("Amount in Words: Rupees Two Hundred Thirty Six Only")
//                .setFontSize(12)
//                .setFontColor(new DeviceRgb(102, 102, 102))
//                .setMarginTop(20));
//
//        // Footer
//        Table footerTable = new Table(UnitValue.createPercentArray(new float[]{2, 1}))
//                .useAllAvailableWidth()
//                .setMarginTop(50);
//
//        // Terms and Conditions
//        Cell termsCell = new Cell();
//        termsCell.add(new Paragraph("Terms & Conditions")
//                .setBold()
//                .setFontSize(13)
//                .setFontColor(new DeviceRgb(51, 51, 51)));
//        termsCell.add(new Paragraph("Payment is due within 15 days\nPlease make checks payable to Rapid Groove")
//                .setFontSize(12)
//                .setFontColor(new DeviceRgb(102, 102, 102))
//                .setMarginTop(5));
//        termsCell.setBorder(Border.NO_BORDER);
//        footerTable.addCell(termsCell);
//
//        // Signature
//        Cell signatureCell = new Cell();
//        signatureCell.add(new Paragraph("Authorized Signatory")
//                .setBold()
//                .setFontSize(13)
//                .setTextAlignment(TextAlignment.RIGHT)
//                .setFontColor(new DeviceRgb(51, 51, 51)));
//        signatureCell.setBorder(Border.NO_BORDER);
//        footerTable.addCell(signatureCell);
//
//        document.add(footerTable);
//
//        document.close();
//    }
//
//    // Updated helper methods with font size parameter
//    private Cell createStyledCell(String content, float fontSize) {
//        Cell cell = new Cell().add(new Paragraph(content).setFontSize(fontSize));
//        cell.setPadding(10);  // Increased padding
//        cell.setFontColor(new DeviceRgb(51, 51, 51));
//        return cell;
//    }
//
//    private void addTotalRow(Table table, String label, String amount, boolean isFinal, float fontSize) {
//        Cell labelCell = new Cell()
//                .add(new Paragraph(label)
//                        .setTextAlignment(TextAlignment.RIGHT)
//                        .setFontSize(fontSize)
//                        .setBold());
//        labelCell.setBorder(Border.NO_BORDER);
//
//        Cell amountCell = new Cell()
//                .add(new Paragraph(amount)
//                        .setTextAlignment(TextAlignment.RIGHT)
//                        .setFontSize(fontSize)
//                        .setBold());
//        if (isFinal) {
//            amountCell.setBackgroundColor(new DeviceRgb(51, 51, 51));
//            amountCell.setFontColor(new DeviceRgb(255, 255, 255));
//        }
//        amountCell.setBorder(Border.NO_BORDER);
//
//        table.addCell(labelCell);
//        table.addCell(amountCell);
//    }

    public void generatePDF(String orderId, String filePath) throws FileNotFoundException {
        OrderDetails orderDetails = orderRepository.findByOrderId(orderId);
        PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

        // Set page margins for better spacing
        document.setMargins(40, 40, 40, 40);

        // Header Section
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                .useAllAvailableWidth()
                .setBorder(Border.NO_BORDER);

        // Left side - Company Info
        Cell leftHeaderCell = new Cell();
        leftHeaderCell.add(new Paragraph("Rapid Groove")
                .setFontSize(18)  // Reduced from 32
                .setBold()
                .setFontColor(new DeviceRgb(51, 51, 51)));
        leftHeaderCell.add(new Paragraph("Tech-Connect Retail Private Limited")
                .setFontSize(10)  // Reduced from 14
                .setFontColor(new DeviceRgb(102, 102, 102))
                .setMarginTop(2));
        leftHeaderCell.add(new Paragraph("GSTIN: 09AAICA4872D1ZM")
                .setFontSize(8)  // Reduced from 12
                .setFontColor(new DeviceRgb(102, 102, 102))
                .setMarginTop(2));
        leftHeaderCell.setBorder(Border.NO_BORDER);
        headerTable.addCell(leftHeaderCell);

        // Right side - Invoice Title and Number
        Cell rightHeaderCell = new Cell();
        rightHeaderCell.add(new Paragraph("TAX INVOICE")
                .setFontSize(16)  // Reduced from 26
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontColor(new DeviceRgb(51, 51, 51)));
        rightHeaderCell.add(new Paragraph("Invoice #: " + orderId)
                .setFontSize(9)  // Reduced from 14
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontColor(new DeviceRgb(102, 102, 102))
                .setMarginTop(2));
        rightHeaderCell.setBorder(Border.NO_BORDER);
        headerTable.addCell(rightHeaderCell);

        document.add(headerTable);

        // Separator line
        document.add(new LineSeparator(new SolidLine(0.5f))  // Reduced line thickness
                .setMarginTop(15)
                .setMarginBottom(15));

        // Address and Details Section
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}))
                .useAllAvailableWidth()
                .setMarginBottom(15);

        // Bill To Section
        Cell billToCell = new Cell();
        billToCell.add(new Paragraph("BILL TO")
                .setBold()
                .setFontSize(9)  // Reduced from 14
                .setFontColor(new DeviceRgb(51, 51, 51))
                .setMarginBottom(5));
        UserAddress billingAddress = orderDetails.getUserAddress();
        billToCell.add(new Paragraph(billingAddress.getName())
                .setFontSize(9)  // Reduced from 13
                .setBold());
        billToCell.add(new Paragraph(billingAddress.getCity() + ", " + billingAddress.getState())
                .setFontSize(8)  // Reduced from 12
                .setMarginTop(2));
        billToCell.add(new Paragraph(billingAddress.getPinCode())
                .setFontSize(8));
        billToCell.add(new Paragraph(billingAddress.getPhoneNo())
                .setFontSize(8)
                .setMarginTop(2));
        billToCell.setBorder(Border.NO_BORDER);
        infoTable.addCell(billToCell);

        // Ship To Section (similar reductions)
        Cell shipToCell = new Cell();
        shipToCell.add(new Paragraph("SHIP TO")
                .setBold()
                .setFontSize(9)
                .setFontColor(new DeviceRgb(51, 51, 51))
                .setMarginBottom(5));
        UserAddress shippingAddress = orderDetails.getUserAddress();
        shipToCell.add(new Paragraph(shippingAddress.getName())
                .setFontSize(9)
                .setBold());
        shipToCell.add(new Paragraph(shippingAddress.getCity() + ", " + shippingAddress.getState())
                .setFontSize(8)
                .setMarginTop(2));
        shipToCell.add(new Paragraph(shippingAddress.getPinCode())
                .setFontSize(8));
        shipToCell.add(new Paragraph(shippingAddress.getPhoneNo())
                .setFontSize(8)
                .setMarginTop(2));
        shipToCell.setBorder(Border.NO_BORDER);
        infoTable.addCell(shipToCell);

        // Invoice Details Section
        Cell invoiceDetailsCell = new Cell();
        invoiceDetailsCell.add(new Paragraph("INVOICE DETAILS")
                .setBold()
                .setFontSize(9)
                .setFontColor(new DeviceRgb(51, 51, 51))
                .setMarginBottom(5));
        invoiceDetailsCell.add(new Paragraph("Invoice Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")))
                .setFontSize(8)
                .setMarginTop(2));
        invoiceDetailsCell.add(new Paragraph("Order Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")))
                .setFontSize(8)
                .setMarginTop(2));
        invoiceDetailsCell.add(new Paragraph("PAN: AAICA4872D")
                .setFontSize(8)
                .setMarginTop(2));
        invoiceDetailsCell.add(new Paragraph("CIN: U52100DL2010PTC202600")
                .setFontSize(8)
                .setMarginTop(2));
        invoiceDetailsCell.setBorder(Border.NO_BORDER);
        infoTable.addCell(invoiceDetailsCell);

        document.add(infoTable);

        // Products Table
        Table productTable = new Table(UnitValue.createPercentArray(new float[]{4, 1, 2, 2, 2}))
                .useAllAvailableWidth()
                .setMarginTop(10);

        // Table Headers
        Stream.of("Product Description", "Qty", "Price", "Tax (18%)", "Total")
                .forEach(title -> {
                    Cell header = new Cell().add(new Paragraph(title)
                            .setBold()
                            .setFontSize(9)
                            .setFontColor(new DeviceRgb(255, 255, 255)));
                    header.setBackgroundColor(new DeviceRgb(51, 51, 51));
                    header.setPadding(6);
                    productTable.addHeaderCell(header);
                });

        // Add products
        for (OrderProductDetails product : orderDetails.getOrderProducts()) {
            productTable.addCell(createStyledCell(product.getProductName(), 8));
            productTable.addCell(createStyledCell(String.valueOf(product.getQuantity()), 8));
            productTable.addCell(createStyledCell("₹200.00", 8));
            productTable.addCell(createStyledCell("₹36.00", 8));
            productTable.addCell(createStyledCell("₹236.00", 8));
        }

        document.add(productTable);

        // Totals Section
        Table totalsTable = new Table(UnitValue.createPercentArray(new float[]{7, 3}))
                .useAllAvailableWidth()
                .setMarginTop(15);

        addTotalRow(totalsTable, "Subtotal:", "₹200.00", false, 9);
        addTotalRow(totalsTable, "Tax (18%):", "₹36.00", false, 9);
        addTotalRow(totalsTable, "Grand Total:", "₹236.00", true, 10);

        document.add(totalsTable);

        // Amount in Words
        document.add(new Paragraph("Amount in Words: Rupees Two Hundred Thirty Six Only")
                .setFontSize(8)
                .setFontColor(new DeviceRgb(102, 102, 102))
                .setMarginTop(10));

        // Footer
        Table footerTable = new Table(UnitValue.createPercentArray(new float[]{2, 1}))
                .useAllAvailableWidth()
                .setMarginTop(25);

        // Terms and Conditions
        Cell termsCell = new Cell();
        termsCell.add(new Paragraph("Terms & Conditions")
                .setBold()
                .setFontSize(9)
                .setFontColor(new DeviceRgb(51, 51, 51)));
        termsCell.add(new Paragraph("Payment is due within 15 days\nPlease make checks payable to Rapid Groove")
                .setFontSize(8)
                .setFontColor(new DeviceRgb(102, 102, 102))
                .setMarginTop(2));
        termsCell.setBorder(Border.NO_BORDER);
        footerTable.addCell(termsCell);

        // Signature
        Cell signatureCell = new Cell();
        signatureCell.add(new Paragraph("Authorized Signatory")
                .setBold()
                .setFontSize(9)
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontColor(new DeviceRgb(51, 51, 51)));
        signatureCell.setBorder(Border.NO_BORDER);
        footerTable.addCell(signatureCell);

        document.add(footerTable);

        document.close();
    }

    // Helper methods with updated padding
    private Cell createStyledCell(String content, float fontSize) {
        Cell cell = new Cell().add(new Paragraph(content).setFontSize(fontSize));
        cell.setPadding(5);  // Reduced padding
        cell.setFontColor(new DeviceRgb(51, 51, 51));
        return cell;
    }

    private void addTotalRow(Table table, String label, String amount, boolean isFinal, float fontSize) {
        Cell labelCell = new Cell()
                .add(new Paragraph(label)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setFontSize(fontSize)
                        .setBold());
        labelCell.setBorder(Border.NO_BORDER);

        Cell amountCell = new Cell()
                .add(new Paragraph(amount)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setFontSize(fontSize)
                        .setBold());
        if (isFinal) {
            amountCell.setBackgroundColor(new DeviceRgb(51, 51, 51));
            amountCell.setFontColor(new DeviceRgb(255, 255, 255));
        }
        amountCell.setBorder(Border.NO_BORDER);

        table.addCell(labelCell);
        table.addCell(amountCell);
    }




}






