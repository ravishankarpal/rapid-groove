package com.rapid.service;

import com.rapid.core.dto.GoogleUserInfo;
import com.rapid.core.entity.User;
import com.rapid.core.enums.AuthProviderEnum;
import com.rapid.dao.UserRepository;
import com.rapid.security.JwtRequestFilter;
import com.rapid.security.JwtTokenDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;


@Service
@Slf4j
public class GoogleAuthServiceImpl implements GoogleAuthService{

    private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

//    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
//    private String redirectUri;

//    @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
//    private String authorizationUri;


    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecretId;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtTokenDetails jwtTokenDetails;


    @Override
    public Map<String, String> initiateGoogleSignIn() {
        String authUrl = UriComponentsBuilder.fromHttpUrl(GOOGLE_AUTH_URL)
                .queryParam("client_id", clientId)
               // .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "email profile")
                .queryParam("access_type", "offline")
                .build()
                .toUriString();

        return Map.of("authUrl", authUrl);

    }

    @Override
    public User handleGoogleCallback(String code) {
        String accessToken = getAccessToken(code);
        return getUserInfo(accessToken);
    }

    private String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecretId);
        body.add("code", code);
       // body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                GOOGLE_TOKEN_URL,
                request,
                Map.class
        );
        return (String) response.getBody().get("access_token");
    }

    private User getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<GoogleUserInfo> response = restTemplate.exchange(
                GOOGLE_USER_INFO_URL,
                HttpMethod.GET,
                entity,
                GoogleUserInfo.class
        );

        GoogleUserInfo userInfo = response.getBody();
        return convertToUser(userInfo);
    }

    private User convertToUser(GoogleUserInfo googleUserInfo) {
       log.info("Creating user by google oauth2");
        User user = new User();
        user.setName(googleUserInfo.getName());
        user.setEmail(googleUserInfo.getEmail());
        user.setAuthProvider(AuthProviderEnum.GOOGLE.getProvider());
        user.setEmailVerified(googleUserInfo.isEmailVerified());
        User createdUser =  userRepository.saveAndFlush(user);
        return createdUser;

    }
}
