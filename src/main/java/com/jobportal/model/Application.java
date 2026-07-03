package com.jobportal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "applications")
public class Application {
    @Id
    private String id;
    
    private String userId; // Reference to User id (Candidate)
    private String recruiterId; // Reference to User id (Recruiter)
    private String jobId; // Reference to Job id
    
    // Denormalized fields for faster queries
    private String candidateName;
    private String candidateEmail;
    private String jobTitle;
    private String companyName;
    
    private ApplicationStatus status; // APPLIED, UNDER_REVIEW, SHORTLISTED, INTERVIEW, SELECTED, REJECTED
    
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}
