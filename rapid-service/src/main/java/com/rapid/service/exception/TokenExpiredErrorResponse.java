package com.rapid.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenExpiredErrorResponse {
    private static  final long serialVersionUID =1L;

    private int code;
    private String message;
    public TokenExpiredErrorResponse(){
        super();
    }

}
