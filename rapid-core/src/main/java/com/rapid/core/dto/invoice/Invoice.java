package com.rapid.core.dto.invoice;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class Invoice implements Serializable {

    private String orderId;
    private String invoiceDate;
    private String orderDate;
    private String shipTo;
    private String billTo;
    private List<LineItem> products;
    private double subtotal;
    private double taxes;
    private double total;
}
