package com.rapid.service;

import com.rapid.core.entity.Role;
import com.rapid.core.entity.User;
import com.rapid.dao.RoleRepository;
import com.rapid.dao.UserRepository;
import com.rapid.security.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    private JwtService jwtService;


    @Override
    public void registerUser(User user) {
        User us = new User();
        Role defaultRole = roleRepository.findByRoleName("User");
        Set<Role> roles = new HashSet<>();
        if (user.getRole() == null || user.getRole().isEmpty()) {
            roles.add(defaultRole);
            user.setRole(roles);
        }else{
            roles = user.getRole();
        }
        us.setName(user.getName());
        us.setEmail(user.getEmail());
        us.setRole(roles);
        us.setPassword(passwordEncoder.encode(user.getPassword()));
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

}
