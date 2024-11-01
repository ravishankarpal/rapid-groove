package com.rapid.service.exception;

public class RapidGrooveException extends Exception{

    public RapidGrooveException(String message) {
        super(message);
    }

    public RapidGrooveException(String message, Throwable cause) {

        super(message, cause);
    }

}
