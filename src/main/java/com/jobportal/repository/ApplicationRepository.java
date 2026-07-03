package com.jobportal.repository;

import com.jobportal.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findByUserId(String userId);
    List<Application> findByCandidateEmail(String candidateEmail);
    List<Application> findByRecruiterId(String recruiterId);
    List<Application> findByJobId(String jobId);
    
    Optional<Application> findByUserIdAndJobId(String userId, String jobId);
}
