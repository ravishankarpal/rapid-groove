package com.rapid.core.dto.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class ShipmentDetails {

    @JsonProperty(value = "tracking_company")
    private String trackingCompany;

    @JsonProperty(value = "tracking_urls")
    private List<String> trackingUrls;

    @JsonProperty(value = "tracking_numbers")
    private String trackingNumbers;




}
