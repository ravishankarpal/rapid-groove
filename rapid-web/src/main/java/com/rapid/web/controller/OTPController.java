package com.rapid.web.controller;


import com.rapid.service.OTPService;
import com.rapid.service.exception.RapidGrooveException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rapid/otp")
public class OTPController {

    @Autowired
    private OTPService otpService;


    @PostMapping("/forgot/password")
    public ResponseEntity<?> forgotPassword(@RequestParam String userName) throws RapidGrooveException, MessagingException {
        otpService.forgotPassword(userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping (value = "/validate-otp")
    public ResponseEntity<?> validateOtp( @RequestParam String userName,@RequestParam String otp) throws Exception{
        String message = otpService.validateOtp(userName,otp);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
}
