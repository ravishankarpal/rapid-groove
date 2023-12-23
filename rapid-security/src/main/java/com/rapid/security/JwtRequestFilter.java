package com.rapid.security;

import com.rapid.security.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenDetails jwtTokenDetails;

    @Autowired
    private JwtService jwtService;

    public static  String CURRENT_USER = "";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse
            response, FilterChain filterChain) throws ServletException,
            IOException {
        final  String header  = request.getHeader("Authorization");
        String token = null;
        String userName  = null;
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            try {
                userName = jwtTokenDetails.getUserDetailFromToken(token);
                CURRENT_USER = userName;

            } catch (IllegalArgumentException e) {
                log.error("Unable to fetch jwt token", e);
            } catch (ExpiredJwtException e) {
                log.error("Jwt token expired", e);
            }
        }else{
            log.info("Header must be start with Bearer");
        }
        if (userName != null && SecurityContextHolder.getContext()
                .getAuthentication() == null){
            UserDetails userDetails = jwtService.loadUserByUsername(userName);
            if (jwtTokenDetails.tokenValidate(token, userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken = new
                        UsernamePasswordAuthenticationToken(userDetails,
                                null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        }
        filterChain.doFilter(request,response);

    }

}
