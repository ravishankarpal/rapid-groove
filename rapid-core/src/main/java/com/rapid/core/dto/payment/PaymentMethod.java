package com.rapid.core.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.WRAPPER_OBJECT
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Card.class, name = "card"),
        @JsonSubTypes.Type(value = UPI.class, name = "upi")
})

@Getter
@Setter

public abstract class PaymentMethod {

    @JsonProperty(value = "channel")
    private String channel;


    public abstract void validate();

}
