package com.rapid.service;

import com.rapid.core.entity.User;
import com.rapid.security.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;



public interface UserService {


    void registerUser(User user);

      void initiateRolesAndUser();
}
