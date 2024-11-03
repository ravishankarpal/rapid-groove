package com.rapid.service;

import com.rapid.core.entity.OtpResetToken;
import com.rapid.core.entity.User;
import com.rapid.service.exception.RapidGrooveException;
import jakarta.mail.MessagingException;

public interface OTPService {
    void forgotPassword(String userName) throws RapidGrooveException, MessagingException;

    String validateOtp(String userName,String otp) throws Exception;

    void generateAndSendOtp(User user, String reason) throws MessagingException;

    boolean isOtpExpired(OtpResetToken otpResetToken);

    void invalidateOtpToken(String userName,String otp);
}
