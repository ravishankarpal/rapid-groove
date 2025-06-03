package com.rapid.core.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;


import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Card  extends PaymentMethod implements Serializable {
    @JsonProperty(value = "card_number")
    private String cardNumber;

    @JsonProperty(value = "card_holder_name")
    private String cardHolderName;

    @JsonProperty(value = "card_expiry_mm")
    private String cardExpiryMM;

    @JsonProperty(value = "card_expiry_yy")
    private String cardExpiryYY;

    @JsonProperty(value = "card_cvv")
    private String cardCvv;


    @Override
    public void validate() {

        if (getChannel() == null || getChannel().isEmpty()) {
            throw new IllegalArgumentException("Channel cannot be empty for Card payment.");
        }
        if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
            throw new IllegalArgumentException("Card number must be a 16-digit number.");
        }
        if (cardHolderName == null || cardHolderName.isEmpty()) {
            throw new IllegalArgumentException("Card holder name cannot be null or empty.");
        }
        if (cardExpiryMM == null || !cardExpiryMM.matches("(0[1-9]|1[0-2])")) {
            throw new IllegalArgumentException("Invalid card expiry month.");
        }
        if (cardExpiryYY == null || !cardExpiryYY.matches("\\d{2}")) {
            throw new IllegalArgumentException("Invalid card expiry year.");
        }
        if (cardCvv == null || !cardCvv.matches("\\d{3}")) {
            throw new IllegalArgumentException("CVV must be a 3-digit number.");
        }
    }


}
