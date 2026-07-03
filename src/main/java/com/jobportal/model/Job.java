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
@Document(collection = "jobs")
public class Job {
    @Id
    private String id;

    private String title;
    private String description;
    private String skillsRequired;
    private String experienceRequired;
    private String salary;
    private String location;

    private String recruiterId; // Reference to User ID of the recruiter
    private String companyName;

    private LocalDateTime postedAt;
}
