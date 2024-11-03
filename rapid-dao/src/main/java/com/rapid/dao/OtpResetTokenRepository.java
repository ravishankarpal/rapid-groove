package com.rapid.dao;

import com.rapid.core.entity.OtpResetToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OtpResetTokenRepository extends JpaRepository<OtpResetToken, Long> {


    @Query(value = "select * from otp_reset where user_id =:email order by id desc limit 1", nativeQuery = true)
    Optional<OtpResetToken> findByUserEmail(String email);
    Optional<OtpResetToken> findByUserEmailAndOtp(String email,String otp);

    @Transactional
    @Modifying
    @Query(value = "update otp_reset set is_used = 1 where user_id =:email and otp =:userOTP", nativeQuery = true)
    void invalidateOTP(String email,String userOTP);

    @Transactional
    void deleteByOtp(String otp);

}
