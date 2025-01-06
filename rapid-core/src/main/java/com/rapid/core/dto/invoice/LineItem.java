package com.rapid.core.dto.invoice;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LineItem implements Serializable {
    private String title;
    //private String description;
    private String unitPrice;
    private String discount;
    private int quantity;
    private String netAmount;
    private String taxRate;
    private String taxAmount;
    private String totalAmount;
    private double grossAmount;
}
