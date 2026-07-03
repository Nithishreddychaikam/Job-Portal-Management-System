package com.jobportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/test-email")
    public String sendTestEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            // Sending it to the email configured in properties for testing
            message.setTo("nithish.jobportal@gmail.com");
            message.setSubject("Test Email - JobPortal");
            message.setText("Spring Boot Email is working successfully.");

            mailSender.send(message);

            System.out.println("Test email sent successfully");
            return "Email sent successfully";
        } catch (Exception e) {
            System.err.println("Failed to send test email: " + e.getMessage());
            e.printStackTrace();
            return "Failed to send email. Error: " + e.getMessage();
        }
    }
}
