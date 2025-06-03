package com.rapid.dao;


import com.rapid.core.entity.payment.PaymentDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentDetailsRepository  extends JpaRepository<PaymentDetails, String> {

    Optional<PaymentDetails> findByTransactionId(String transactionId);

}
