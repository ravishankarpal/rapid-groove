package com.rapid.service;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public interface CustomOAuth2UserService {

    Map<String, Object> user(OAuth2User oAuth2User);

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException;

}
