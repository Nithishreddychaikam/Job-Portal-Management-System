package com.jobportal.service;

import com.jobportal.model.OTPVerification;
import com.jobportal.repository.OTPVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OTPService {

    private final OTPVerificationRepository otpRepository;

    public String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    public void saveOtp(String email, String otp) {
        OTPVerification otpEntity = otpRepository.findByEmail(email);
        if (otpEntity == null) {
            otpEntity = new OTPVerification();
            otpEntity.setEmail(email);
        }
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(10));

        otpRepository.save(otpEntity);
    }
}
