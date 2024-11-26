package com.rapid.web.controller;

import com.rapid.core.entity.User;
import com.rapid.service.GoogleAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController

@RequestMapping(value = "/rapid/oauath2")
public class GoogleAuthController {

   @Autowired
   private GoogleAuthService googleAuthService;




    @PostMapping("/google")
    public ResponseEntity<Map<String, String>> initiateGoogleSignIn(){

        Map<String, String> initiateGoogleSignIn   = googleAuthService.initiateGoogleSignIn();
        return new ResponseEntity<>(initiateGoogleSignIn, HttpStatus.OK);
    }

    @GetMapping("/google/callback")
    public ResponseEntity<User> handleGoogleCallback(@RequestParam("code") String code) {
        User userDetails =  googleAuthService.handleGoogleCallback(code);
        return new ResponseEntity<>(userDetails, HttpStatus.OK);

    }

}
