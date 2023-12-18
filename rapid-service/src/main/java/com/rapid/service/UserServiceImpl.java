package com.rapid.service;

import com.rapid.core.entity.Role;
import com.rapid.core.entity.User;
import com.rapid.dao.RoleRepository;
import com.rapid.dao.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserServiceImpl implements  UserService{
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public void registerUser(User user) {
        User us = new User();
        Role role = roleRepository.findById("User").get();
        Set<Role> userRole  = new HashSet<>();
        userRole.add(role);
        us.setUserName(user.getUserName());
        us.setUserFirstName(user.getUserFirstName());
        us.setUserLastName(user.getUserLastName());
        us.setRole(userRole);
        us.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        userRepository.saveAndFlush(us);

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
        adminUser.setUserName("suku17");
        adminUser.setUserFirstName("Sukanya");
        adminUser.setUserLastName("Mandal");
        adminUser.setUserPassword(passwordEncoder.encode("Suku@23"));
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        userRepository.saveAndFlush(adminUser);

        User user = new User();
        adminUser.setUserName("raj_6732");
        adminUser.setUserName("Raj");
        adminUser.setUserLastName("Sharma");
        adminUser.setUserPassword(passwordEncoder.encode("Raj@67"));
        Set<Role> userRoles = new HashSet<>();
        adminRoles.add(userRole);
        userRepository.saveAndFlush(user);

    }

}
