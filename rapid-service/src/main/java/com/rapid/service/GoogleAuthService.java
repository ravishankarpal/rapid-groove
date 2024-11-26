package com.rapid.service;

import com.rapid.core.entity.User;

import java.util.Map;

public interface GoogleAuthService {
    Map<String, String> initiateGoogleSignIn();

    User handleGoogleCallback(String code);
}
