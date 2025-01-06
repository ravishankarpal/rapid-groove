package com.rapid.service.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenExpiredException extends RuntimeException {
    private static  final long serialVersionUID =1L;

    private TokenExpiredErrorResponse errorResponse;
    public TokenExpiredException(String message) {
        super(message);
    }
    public TokenExpiredException(TokenExpiredErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }
}
