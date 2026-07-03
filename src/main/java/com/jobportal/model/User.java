package com.jobportal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {
    @Id
    private String id;
    
    private String name;
    
    @Indexed(unique = true)
    private String email;
    
    private String password;
    
    private String role; // ADMIN, CANDIDATE, RECRUITER
    
    private boolean isVerified;
    

    
    private String mobile;
    
    // Candidate Specific fields
    private String skills;
    private String experience;
    private String location;
    private String resumeFileId; // GridFS file id
    
    // Recruiter Specific fields
    private String companyName;
    private String companyDetails;

    private LocalDateTime createdAt;
}
