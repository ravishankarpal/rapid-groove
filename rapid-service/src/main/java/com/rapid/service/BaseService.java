package com.rapid.service;

import com.rapid.security.JwtRequestFilter;
import com.rapid.service.exception.TokenExpiredErrorResponse;
import com.rapid.service.exception.TokenExpiredException;

public abstract class BaseService {

    protected void checkTokenExpiration() throws TokenExpiredException {
        if (JwtRequestFilter.IS_TOKEN_EXPIRED) {
            throw new TokenExpiredException(
                    new TokenExpiredErrorResponse(401, "Session has expired. Please log in again")
            );
        }
    }
}
