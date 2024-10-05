package com.rapid.core.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginDto implements Serializable {
    private String email;
    private String password;
}
