package com.rapid.security.service;

import com.rapid.core.dto.JwtRequest;
import com.rapid.core.dto.JwtResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface JwtService {

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

     JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception ;

}
