package com.rapid.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class RapidGrooveErrorResponse implements Serializable {
    private static  final long serialVersionUID =1L;

    private int code;
    private String message;
    public RapidGrooveErrorResponse(){
        super();
    }


}
