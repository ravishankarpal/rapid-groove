package com.rapid.web.controller;

import com.rapid.core.dto.JwtRequest;
import com.rapid.core.dto.JwtResponse;
import com.rapid.security.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
//@RequestMapping(value = "jwt")
public class JwtController {

    @Autowired
    private JwtService jwtService;


    @PostMapping(value = "/authenticate")

    public ResponseEntity<JwtResponse> createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        JwtResponse response = jwtService.createJwtToken(jwtRequest);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
}
