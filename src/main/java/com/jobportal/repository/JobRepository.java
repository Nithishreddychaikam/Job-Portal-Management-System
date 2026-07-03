package com.jobportal.repository;

import com.jobportal.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {
    List<Job> findByRecruiterId(String recruiterId);
    
    // Derived query methods for searching
    List<Job> findByLocationContainingIgnoreCaseOrSkillsRequiredContainingIgnoreCaseOrTitleContainingIgnoreCase(String location, String skills, String title);
}
