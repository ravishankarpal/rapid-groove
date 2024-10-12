package com.rapid.web.controller;
import com.rapid.core.dto.LoginDto;
import com.rapid.core.dto.UserAddressDTO;
import com.rapid.core.entity.DeliveryAvailability;
import com.rapid.core.entity.Role;
import com.rapid.core.entity.User;
import com.rapid.service.UserService;
import com.rapid.service.exception.RapidGrooveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;


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


    @GetMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto){
       UserDetails userDetails =  userService.loginUser(loginDto);
        return new ResponseEntity<>(userDetails, HttpStatus.OK);
    }

    @GetMapping("/forAdmin")
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

    @GetMapping(value = "/check/delivery/{pinCode}")
    public ResponseEntity<?> checkDeliveryAvailableOrNot(@PathVariable("pinCode") String pinCode){
        DeliveryAvailability isDeliveryAvailableOrNot = userService.checkDeliveryAvailableOrNot(pinCode);
        if (isDeliveryAvailableOrNot != null) {
            return new ResponseEntity<>(isDeliveryAvailableOrNot, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new RapidGrooveException("Delivery not available at pincode - "+  pinCode), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/save/address")
    public ResponseEntity<?> saveUserAddressDetails(@RequestBody UserAddressDTO userAddressDTO){
        userService.saveUserAddressDetails(userAddressDTO);
     return  new ResponseEntity<>(HttpStatus.OK);

    }





}
