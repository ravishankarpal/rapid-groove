package com.rapid.service;

import com.rapid.security.JwtRequestFilter;
import com.rapid.service.exception.TokenExpiredErrorResponse;
import com.rapid.service.exception.TokenExpiredException;
import org.apache.commons.lang3.StringUtils;

public abstract class BaseService {

    protected void checkTokenExpiration() throws TokenExpiredException {

        if (StringUtils.isBlank(JwtRequestFilter.CURRENT_USER)) {
            throw new TokenExpiredException(
                    new TokenExpiredErrorResponse(400, "User has not logged in!")
            );
        }

        if (JwtRequestFilter.IS_TOKEN_EXPIRED) {
            throw new TokenExpiredException(
                    new TokenExpiredErrorResponse(401, "Session has expired. Please log in again")
            );
        }
    }
}
