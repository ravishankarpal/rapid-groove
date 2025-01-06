package com.rapid.service.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class RapidGrooveUnAuthorizedException extends RuntimeException {
    private static  final long serialVersionUID =1L;

    private RapidGrooveErrorResponse errorResponse;
    public RapidGrooveUnAuthorizedException(String message) {
        super(message);
    }

    public RapidGrooveUnAuthorizedException(RapidGrooveErrorResponse errorResponse){
        this.errorResponse = errorResponse;
    }

}
