package com.rapid.core.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Getter
@Service
@Setter
@NoArgsConstructor

public class RapidGrooveAPIResponse {
    private HttpStatus code;
    private String message;
}
