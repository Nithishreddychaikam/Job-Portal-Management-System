package com.jobportal.controller;

import com.jobportal.dto.UserDto;
import com.jobportal.model.OTPVerification;
import com.jobportal.model.User;
import com.jobportal.repository.OTPVerificationRepository;
import com.jobportal.repository.UserRepository;
import com.jobportal.service.EmailService;
import com.jobportal.service.OTPService;
import com.jobportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final OTPService otpService;
    private final EmailService emailService;
    private final OTPVerificationRepository otpRepository;
    private final UserRepository userRepository;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register/candidate")
    public String registerCandidatePage(Model model) {
        UserDto dto = new UserDto();
        dto.setRole("CANDIDATE");
        model.addAttribute("userDto", dto);
        return "register-candidate";
    }

    @GetMapping("/register/recruiter")
    public String registerRecruiterPage(Model model) {
        UserDto dto = new UserDto();
        dto.setRole("RECRUITER");
        model.addAttribute("userDto", dto);
        return "register-recruiter";
    }

    @PostMapping(value = "/register/candidate", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<?> registerCandidate(@RequestBody UserDto dto) {
        dto.setRole("CANDIDATE");
        return processRegistrationAjax(dto);
    }

    @PostMapping(value = "/register/recruiter", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<?> registerRecruiter(@RequestBody UserDto dto) {
        dto.setRole("RECRUITER");
        return processRegistrationAjax(dto);
    }

    private ResponseEntity<?> processRegistrationAjax(UserDto userDto) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match!");
        }

        User user = userService.findByEmail(userDto.getEmail()).orElse(null);
        if (user != null) {
            if (user.isVerified()) {
                return ResponseEntity.badRequest().body("Email already exists!");
            }
            // Existing unverified user: update details
            user.setName(userDto.getName());
            user.setMobile(userDto.getPhoneNumber() != null ? userDto.getPhoneNumber() : userDto.getMobile());
            user.setRole(userDto.getRole());
            user.setPassword(userDto.getPassword()); 
            if ("RECRUITER".equals(userDto.getRole())) {
                user.setCompanyName(userDto.getCompanyName());
                user.setCompanyDetails(userDto.getCompanyDetails());
            }
        } else {
            // New user
            user = User.builder()
                    .name(userDto.getName())
                    .email(userDto.getEmail())
                    .password(userDto.getPassword())
                    .role(userDto.getRole())
                    .mobile(userDto.getPhoneNumber() != null ? userDto.getPhoneNumber() : userDto.getMobile())
                    .isVerified(false)
                    .build();

            if ("RECRUITER".equals(userDto.getRole())) {
                user.setCompanyName(userDto.getCompanyName());
                user.setCompanyDetails(userDto.getCompanyDetails());
            }
        }

        user.setVerified(false);
        user = userService.saveUser(user);

        // Generate and send OTP
        String otp = otpService.generateOtp();
        otpService.saveOtp(user.getEmail(), otp);
        emailService.sendOtp(user.getEmail(), otp);

        return ResponseEntity.ok("OTP sent successfully");
    }

    @GetMapping("/verify-otp")
    public String verifyOtpPage(Model model, @ModelAttribute("email") String email) {
        if (email == null || email.isEmpty()) {
            return "redirect:/login";
        }
        model.addAttribute("email", email);
        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    @ResponseBody
    public ResponseEntity<?> verifyOtp(@RequestParam String email,
                                       @RequestParam String otp) {

        OTPVerification stored = otpRepository.findByEmail(email);

        if (stored == null || !stored.getOtp().equals(otp)) {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }

        if (stored.getExpiryTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("OTP expired");
        }

        User user = userService.findByEmail(email).orElse(null);
        if (user != null) {
            user.setVerified(true);
            userRepository.save(user);
        }

        otpRepository.delete(stored);

        return ResponseEntity.ok("Verified successfully");
    }

    @PostMapping("/resend-otp")
    @ResponseBody
    public ResponseEntity<?> resendOtp(@RequestParam String email) {
        String otp = otpService.generateOtp();
        otpService.saveOtp(email, otp);
        emailService.sendOtp(email, otp);

        return ResponseEntity.ok("OTP resent");
    }
}
