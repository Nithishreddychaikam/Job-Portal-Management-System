package com.jobportal.repository;

import com.jobportal.model.OTPVerification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPVerificationRepository extends MongoRepository<OTPVerification, String> {
    OTPVerification findByEmail(String email);
}
