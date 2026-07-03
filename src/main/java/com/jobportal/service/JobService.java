package com.jobportal.service;

import com.jobportal.model.Job;
import com.jobportal.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    
    public Job saveJob(Job job) {
        job.setPostedAt(LocalDateTime.now());
        return jobRepository.save(job);
    }
    
    public Optional<Job> findById(String id) {
        return jobRepository.findById(id);
    }
    
    public List<Job> findAll() {
        return jobRepository.findAll();
    }
    
    public List<Job> findByRecruiterId(String recruiterId) {
        return jobRepository.findByRecruiterId(recruiterId);
    }
    
    public List<Job> searchJobs(String keyword) {
        if(keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        return jobRepository.findByLocationContainingIgnoreCaseOrSkillsRequiredContainingIgnoreCaseOrTitleContainingIgnoreCase(keyword, keyword, keyword);
    }
    
    public void deleteJob(String id) {
        jobRepository.deleteById(id);
    }
    
    public long count() {
        return jobRepository.count();
    }
}
