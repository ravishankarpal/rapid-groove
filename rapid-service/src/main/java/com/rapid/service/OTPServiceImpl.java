package com.rapid.service;


import com.rapid.core.dto.Constant;
import com.rapid.core.entity.OtpResetToken;
import com.rapid.core.entity.User;
import com.rapid.dao.OtpResetTokenRepository;
import com.rapid.dao.UserRepository;
import com.rapid.service.exception.RapidGrooveException;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Slf4j
public class OTPServiceImpl implements OTPService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    private OtpResetTokenRepository otpResetTokenRepository;

    @Autowired
    private EmailService emailService;


    @Override
    public void forgotPassword(String userName) throws RapidGrooveException, MessagingException {
        log.info("Password reset requested for user: {}", userName);
        User user = userRepository.findById(userName).orElseThrow(() -> new RapidGrooveException("User has not found"));
        generateAndSendOtp(user, Constant.FORGOT_PASSWORD);

    }

    @Override
    public void generateAndSendOtp(User user, String reason) throws MessagingException {
        log.info("Going to generate OTP for user {} has forgotted password",user.getEmail());
        OtpResetToken otpToken = new OtpResetToken();
        OtpResetToken otpResetToken = otpResetTokenRepository.findByUserEmail(user.getEmail()).orElse(null);
        String otp = null;
        if (isOtpExpired(otpResetToken)){
            otp= generateOtp();
            otpToken.setOtp(otp);
            otpToken.setIsUsed(0);
            otpToken.setUser(user);
            otpToken.setExpiryDate(LocalDateTime.now().plusMinutes(2));
            otpResetTokenRepository.saveAndFlush(otpToken);

        }else{
            otp = otpToken.getOtp();
        }
        emailService.sendOTPEmail(user,otp);
    }

    @Override
    public boolean isOtpExpired(OtpResetToken otpResetToken) {
        LocalDateTime currentDate = LocalDateTime.now();
        return otpResetToken.getExpiryDate().isBefore(currentDate);
    }


    @Override
    public String validateOtp(String userName, String otp) throws Exception {
        try {
            log.info("Validating OTP {} for user {}" , otp, userName);
            OtpResetToken otpResetToken = otpResetTokenRepository.findByUserEmailAndOtp(userName,otp).orElse(null);

            if(otp == null){
                return "Please Enter the OTP!";
            }
            else if (otpResetToken == null || otpResetToken.getOtp() == null ||
            BooleanUtils.toBoolean(otpResetToken.getIsUsed())) {
                return "Invalid OTP!";
            }

            else if (!otp.equals(otpResetToken.getOtp())) {
                return "Invalid OTP!";
            }

            invalidateOtpToken(userName,otp);
        }catch (Exception e){
            log.error("Exception while validating OTP {}", otp, e);
            return "Something Went Wrong!";
        }
        return "Valid OTP!";
    }

    private String generateOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < Constant.OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    @Override
    public void invalidateOtpToken(String userName,String otp){

        otpResetTokenRepository.invalidateOTP(userName,otp);
    }

}
