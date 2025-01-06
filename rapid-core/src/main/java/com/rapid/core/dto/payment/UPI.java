package com.rapid.core.dto.payment;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
@Setter

public class UPI extends PaymentMethod implements Serializable {

    @JsonProperty(value = "upi_id")
    private String upiId;

    @JsonProperty(value = "upi_expiry_minutes")
    private Integer upiExpiryMinutes;

    @Override
    public void validate() {
        if (getChannel() == null || getChannel().isEmpty()) {
            throw new IllegalArgumentException("Channel cannot be null or empty for UPI payment.");
        }
        if (StringUtils.isBlank(upiId) || !upiId.matches("^[\\w.+-]+@[\\w.-]+$")) {
            throw new IllegalArgumentException("Invalid UPI ID format.");
        }
        if (getUpiExpiryMinutes() == null || getUpiExpiryMinutes() <= 0) {
            throw new IllegalArgumentException("UPI expiry minutes must be greater than 0.");
        }
    }
}
