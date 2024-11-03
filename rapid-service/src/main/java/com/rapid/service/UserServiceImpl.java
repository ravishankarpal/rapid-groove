package com.rapid.service;

import com.rapid.core.dto.Constant;
import com.rapid.core.dto.LoginDto;
import com.rapid.core.dto.UserAddressDTO;
import com.rapid.core.dto.UserResponse;
import com.rapid.core.entity.*;
import com.rapid.core.exception.InvalidCredentialsException;
import com.rapid.dao.*;
import com.rapid.security.JwtRequestFilter;
import com.rapid.security.service.JwtService;
import com.rapid.service.exception.RapidGrooveException;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements  UserService{
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DeliveryAvailabilityRepository deliveryAvailabilityRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private OtpResetTokenRepository otpResetTokenRepository;

    @Autowired
    private EmailService emailService;


    @Override
    public UserResponse registerUser(User user) {
        UserResponse userResponse = new UserResponse();
        try {
            Optional<User> existingUser = userRepository.findById(user.getEmail());

            if (existingUser.isPresent()) {
                userResponse.setCode(HttpStatus.BAD_REQUEST);
                userResponse.setMessage("User already exist!");
                return  userResponse;
            }
            User us = new User();
            Role defaultRole = roleRepository.findByRoleName("User");
            Set<Role> roles = new HashSet<>();
            if (user.getRole() == null || user.getRole().isEmpty()) {
                roles.add(defaultRole);
                user.setRole(roles);
            } else {
                roles = user.getRole();
            }
            us.setName(user.getName());
            us.setEmail(user.getEmail());
            us.setRole(roles);
            us.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.saveAndFlush(us);
            userResponse.setCode(HttpStatus.OK);
            userResponse.setMessage("User registered successfully!");
        }
        catch (Exception e){
            log.error("Exception occurred while sign up the user {}",user.getEmail() , e);
            userResponse.setCode(HttpStatus.BAD_REQUEST);
            userResponse.setMessage("User registered failed");
        }
        return userResponse;

    }

    @Override
    public  void initiateRolesAndUser(){
        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescriptions("Admin Role");
        roleRepository.saveAndFlush(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("User");
        userRole.setRoleDescriptions("User Role");
        roleRepository.saveAndFlush(userRole);

        User adminUser = new User();
        adminUser.setEmail("suku17@gmail.com");
        adminUser.setName("Sukanya");

        adminUser.setPassword(passwordEncoder.encode("Suku@23"));
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        userRepository.saveAndFlush(adminUser);

        User user = new User();
        adminUser.setEmail("raj6732@gmail.com");
        adminUser.setName("Raj");
        adminUser.setPassword(passwordEncoder.encode("Raj@67"));
        Set<Role> userRoles = new HashSet<>();
        adminRoles.add(userRole);
        userRepository.saveAndFlush(user);

    }

    @Override
    public UserDetails loginUser(LoginDto loginDto){
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getEmail());
        if (userDetails == null || !passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        return userDetails;

    }

    @Override
    public DeliveryAvailability checkDeliveryAvailableOrNot(String pinCode) {

        DeliveryAvailability deliveryAvailability = deliveryAvailabilityRepository.findByPinCode(pinCode);
        if (deliveryAvailability != null && BooleanUtils.toBoolean(deliveryAvailability.getDeliveryAvailable())) {
            return deliveryAvailability;
        }

        return  null;
    }

    @Override
    public void saveUserAddressDetails(UserAddressDTO userAddressDTO){
        String userName = JwtRequestFilter.CURRENT_USER;
        log.info("Request received to save user address details for user name {}", userName);

        if(Objects.nonNull(userAddressDTO) && StringUtils.isNotBlank( userName)) {
            UserAddress userAddress = new UserAddress(userAddressDTO);
            Optional<User> user=  userRepository.findById(userName);
            if (user.isPresent()){
                userAddress.setUser(user.get());
                userAddressRepository.saveAndFlush(userAddress);
            }else {
                log.info("User details add successfully");
            }
        }else{
            log.info("User details can not be blank");
        }

    }

    @Override
    public void updatePassword(String otp, String password) {




    }

}
