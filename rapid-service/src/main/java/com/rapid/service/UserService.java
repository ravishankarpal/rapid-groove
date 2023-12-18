package com.rapid.service;

import com.rapid.core.entity.User;
import org.springframework.stereotype.Service;



public interface UserService {
    void registerUser(User user);

      void initiateRolesAndUser();
}
