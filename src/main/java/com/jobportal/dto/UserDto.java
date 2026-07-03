package com.jobportal.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserDto {
    private String id;
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private String role;
    private String mobile;
    private String phoneNumber;
    private boolean isVerified;
    
    private String skills;
    private String experience;
    private String location;
    
    private String companyName;
    private String companyDetails;
    
    private MultipartFile resumeFile;
}
