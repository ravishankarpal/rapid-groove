package com.rapid.service;

import com.rapid.core.dto.ComplainDTO;
import com.rapid.core.dto.LoginDto;
import com.rapid.core.dto.UserAddressDTO;
import com.rapid.core.dto.UserResponse;
import com.rapid.core.entity.DeliveryAvailability;
import com.rapid.core.entity.User;
import com.rapid.core.entity.UserAddress;
import com.rapid.service.exception.RapidGrooveException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;


public interface UserService {


    UserResponse registerUser(User user) throws RapidGrooveException;

      void initiateRolesAndUser();

    UserDetails loginUser(LoginDto loginDto);

    DeliveryAvailability checkDeliveryAvailableOrNot(String pinCode) ;

    void saveUserAddressDetails(UserAddressDTO userAddressDTO);

    void updatePassword(String otp, String password);


    void registerComplain(ComplainDTO complainDTO) throws RapidGrooveException ;

    List<UserAddress> getUserAddressDetails();

    void deleteUserAddress(Integer id);

    void updateUserAddressDetails(Integer id, UserAddressDTO userAddressDTO);
}
