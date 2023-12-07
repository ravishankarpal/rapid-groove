package com.rapid.web.controller;

import com.rapid.core.entity.User;
import com.rapid.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rapid/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register_user")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        userService.registerUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostConstruct
    public void initiateRolesAndUser(){
        userService.initiateRolesAndUser();
    }
}
