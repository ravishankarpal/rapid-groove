package com.rapid.core.enums;


import lombok.Getter;

@Getter

public enum AuthProviderEnum {

    GOOGLE("Google"),
    LOCAL("Local");

    private final String provider;

    AuthProviderEnum(String provider){
        this.provider = provider;
    }
}
