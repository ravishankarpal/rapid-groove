package com.rapid.service;

import com.rapid.core.dto.*;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private UserComplainRepository userComplainRepository;

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
    public void updatePassword(String userName, String password) {
        log.info("Updating the password for user {}",userName);
        User user = userRepository.findById(userName).orElse(null);
        if(user == null){
            log.error("User {} not found. Password update failed.", userName);
            throw new UsernameNotFoundException("User not found: " + userName);

        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.saveAndFlush(user);

    }

    @Override
    public void registerComplain(ComplainDTO complainDTO) throws RapidGrooveException {
        if(complainDTO == null || complainDTO.getUserId() == null){
            log.info("User can not be null to register complain");
            return;
        }
        try {
            log.info("Going to register a complain for user {}", complainDTO.getUserId());
            UserComplain userComplain = new UserComplain();
            User user = userRepository.findById(complainDTO.getUserId()).orElseThrow(()->new RuntimeException("User not found"));
            userComplain.setUser(user);
            userComplain.setOrderNumber(complainDTO.getOrderNumber());
            userComplain.setDepartment(complainDTO.getDepartment());
            userComplain.setSubject(complainDTO.getSubject());
            userComplain.setMessage(complainDTO.getMessage());
            FileAttachment fileAttachment = new FileAttachment(complainDTO.getAttachment());
            userComplain.setFileAttachment(fileAttachment);
            userComplainRepository.saveAndFlush(userComplain);
        }catch (Exception e){
            throw new RapidGrooveException("Something went wrong ! Please try again");

        }


    }

    @Override
    public List<UserAddress> getUserAddressDetails() {

        String currentUser = JwtRequestFilter.CURRENT_USER;
        User user = userRepository.findById(currentUser).orElseThrow(()-> new RuntimeException("User Not found"));

        List<UserAddress> userAddresses = userAddressRepository.findByUser(user);

        for (UserAddress userAddress : userAddresses){
            user = null;
            userAddress.setUser(user);
        }
        return  userAddresses;

    }

    @Override
    public void deleteUserAddress(Integer id) {
        String currentUser = JwtRequestFilter.CURRENT_USER;
        User user = userRepository.findById(currentUser).orElseThrow(()-> new RuntimeException("User Not found"));
        userAddressRepository.deleteById(id);
    }

    @Override
    public void updateUserAddressDetails(Integer id, UserAddressDTO userAddressDTO) {
        String userName = JwtRequestFilter.CURRENT_USER;
        log.info("Request received to update user address details for user name {}", userName);
        if (id != null && Objects.nonNull(userAddressDTO) && StringUtils.isNotBlank(userName)) {
            UserAddress userAddress = userAddressRepository.findById(id).
                    orElseThrow(() -> new RuntimeException("User Address not found"));
            UserAddress address = new UserAddress(userAddressDTO);
            address.setId(userAddress.getId());
            address.setUser(userAddress.getUser());
            userAddressRepository.saveAndFlush(address);

        }
    }

}
