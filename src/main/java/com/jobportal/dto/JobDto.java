package com.jobportal.dto;

import lombok.Data;

@Data
public class JobDto {
    private String id;
    private String title;
    private String description;
    private String skillsRequired;
    private String experienceRequired;
    private String salary;
    private String location;
    private String recruiterId;
    private String companyName;
}
