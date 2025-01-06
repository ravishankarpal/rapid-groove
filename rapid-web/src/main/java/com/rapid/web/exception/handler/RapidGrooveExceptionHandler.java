package com.rapid.web.exception.handler;

import com.rapid.service.exception.TokenExpiredErrorResponse;
import com.rapid.service.exception.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
@Slf4j
public class RapidGrooveExceptionHandler {
    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public TokenExpiredErrorResponse handleTokenExpiredException(TokenExpiredException ex) {
        if (ex.getErrorResponse() != null) {
            return ex.getErrorResponse();
        }
        // Fallback if errorResponse is null
        return new TokenExpiredErrorResponse(401, ex.getMessage());
    }
}
