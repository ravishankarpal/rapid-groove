package com.rapid.service;

import com.rapid.core.entity.Role;
import com.rapid.core.entity.User;
import com.rapid.dao.RoleRepository;
import com.rapid.dao.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserServiceImpl implements  UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Override
    public void registerUser(User user) {

        userRepository.saveAndFlush(user);

    }

    @Override
    public  void initiateRolesAndUser(){
        Role adminRole = new Role();
        adminRole.setRoleName("Ravi Shankar");
        adminRole.setRoleDescriptions("Admin Role");
        roleRepository.saveAndFlush(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("Rajat");
        userRole.setRoleDescriptions("User");
        roleRepository.saveAndFlush(userRole);

        User adminUser = new User();
        adminUser.setUserName("ravi_6732");
        adminUser.setUserName("Ravi");
        adminUser.setUserLastName("Shankar");
        adminUser.setUserPassword("Ra@673");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        userRepository.saveAndFlush(adminUser);

        User user = new User();
        adminUser.setUserName("sha_6732");
        adminUser.setUserName("Rajat");
        adminUser.setUserLastName("Sharma");
        adminUser.setUserPassword("Rajat@673");
        Set<Role> userRoles = new HashSet<>();
        adminRoles.add(userRole);
        userRepository.saveAndFlush(user);

    }

}
