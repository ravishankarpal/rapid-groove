package com.rapid.service;

import com.rapid.core.entity.User;
import com.rapid.dao.UserRepository;
import jakarta.security.auth.message.config.AuthConfigProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.security.AuthProvider;
import java.security.Provider;
import java.util.Map;

@Service
@Slf4j

public class CustomOAuth2UserServiceImpl extends DefaultOAuth2UserService implements CustomOAuth2UserService {


    @Autowired
    private UserRepository userRepository;

    @Override
    public Map<String, Object> user(OAuth2User oAuth2User) {
        log.info("User loading");
        return Map.of(
                "name", oAuth2User.getAttribute("name"),
                "email", oAuth2User.getAttribute("email")
        );
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        log.info("Loading user {} by outh2",email);

        userRepository.findById(email).orElseGet(() -> {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setAuthProvider("Google");
            return userRepository.saveAndFlush(user);
        });

        return oauth2User;

    }



}
