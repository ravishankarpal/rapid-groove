package com.rapid.web.controller;
import com.rapid.core.dto.ComplainDTO;
import com.rapid.core.dto.LoginDto;
import com.rapid.core.dto.UserAddressDTO;
import com.rapid.core.dto.UserResponse;
import com.rapid.core.entity.DeliveryAvailability;
import com.rapid.core.entity.User;
import com.rapid.core.entity.UserAddress;
import com.rapid.service.UserService;
import com.rapid.service.exception.RapidGrooveException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/rapid/user")
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) throws RapidGrooveException {
           UserResponse userResponse = userService.registerUser(user);
           return new ResponseEntity<>(userResponse,HttpStatus.OK);

    }


    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestParam String userName, @RequestParam String password) throws RapidGrooveException, MessagingException {
        userService.updatePassword(userName, password);
        return new ResponseEntity<>(HttpStatus.OK);
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

    @PostMapping(value = "/register/complain")
    public ResponseEntity<?> registerComplain(@RequestBody ComplainDTO complainDTO) throws RapidGrooveException {
        try {
            userService.registerComplain(complainDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (RapidGrooveException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping (value = "/address-details")
    public ResponseEntity<?> getUserAddressDetails(){
        List<UserAddress> userAddresses =  userService.getUserAddressDetails();
        return  new ResponseEntity<>(userAddresses,HttpStatus.OK);

    }

    @DeleteMapping(value = "delete-address/{id}")
    public ResponseEntity<?> deleteUserAddress(@PathVariable("id") Integer id){
        userService.deleteUserAddress(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping (value = "/update-address/{id}")
    public ResponseEntity<?> updateUserAddressDetails(@PathVariable("id") Integer id,
                                                      @RequestBody UserAddressDTO userAddressDTO){
        userService.updateUserAddressDetails(id,userAddressDTO);
        return  new ResponseEntity<>(HttpStatus.OK);

    }




}
