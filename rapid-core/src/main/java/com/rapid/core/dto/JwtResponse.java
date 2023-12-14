package com.rapid.core.dto;


import com.rapid.core.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class JwtResponse {

    private User user;

    private String jwtToken;
}
