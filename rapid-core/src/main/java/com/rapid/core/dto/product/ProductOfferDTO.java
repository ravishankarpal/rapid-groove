package com.rapid.core.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter


public class ProductOfferDTO {

    private String offerName;

    private Double discountPercentage;

    private LocalDate validFrom;

    private LocalDate validTo;

    // Optional description for the offer
    private String description;
}
