package com.rapid.core.dto.orders;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CustomerDetails {

    @JsonProperty(value = "customer_id")
    private String customerId;

    @JsonProperty(value = "customer_email")
    private String customerEmail;

    @JsonProperty(value = "customer_phone")
    private String customerPhone;

    @JsonProperty(value = "customer_name")
    private String customerName;

    @JsonProperty(value = "customer_uid")
    private String customerUid;

}
