package com.rapid.web.controller;
import com.rapid.core.entity.User;
import com.rapid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


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
    @GetMapping("/forAdmin")
    @PreAuthorize("hasRole('Admin')")

    public String forAdmin(){
        return "This URL is only accessible to the admin";
    }

    @GetMapping({"/forUser"})
    @PreAuthorize("hasRole('User')")
    public String forUser(){
        return "This URL is only accessible to the user";
    }

  // @PostConstruct
    public void initiateRolesAndUser(){
        userService.initiateRolesAndUser();
    }
}
