package com.jobportal.dto;

import lombok.Data;

@Data
public class ApplicationDto {
    private String id;
    private String candidateId;
    private String recruiterId;
    private String jobId;
    private String candidateName;
    private String candidateEmail;
    private String jobTitle;
    private String companyName;
    private String status;
}
