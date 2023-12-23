package com.rapid.service.exception;

public class ProductDetailsNotFoundException extends RuntimeException{
    public ProductDetailsNotFoundException(String message) {
        super(message);
    }

    public ProductDetailsNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
