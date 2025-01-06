package com.rapid.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryPartner {

    DEFAULT_CARRIER("Default Carrier"),
    NOT_ASSIGNED("Not Assigned"),
    WAREHOUSE("Warehouse");
    private String value;

}
