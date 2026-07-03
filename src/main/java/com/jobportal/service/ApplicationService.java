package com.jobportal.service;

import com.jobportal.model.Application;
import com.jobportal.model.ApplicationStatus;
import com.jobportal.model.ApplicationStatusHistory;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.ApplicationStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ApplicationStatusHistoryRepository historyRepository;
    private final EmailService emailService;
    
    public Application applyForJob(Application application) {
        application.setAppliedAt(LocalDateTime.now());
        application.setUpdatedAt(LocalDateTime.now());
        application.setStatus(ApplicationStatus.APPLIED);
        Application saved = applicationRepository.save(application);
        
        saveHistory(saved.getId(), ApplicationStatus.APPLIED, saved.getUserId());
        
        return saved;
    }
    
    private void saveHistory(String applicationId, ApplicationStatus status, String updatedBy) {
        ApplicationStatusHistory history = ApplicationStatusHistory.builder()
                .applicationId(applicationId)
                .status(status)
                .updatedBy(updatedBy)
                .updatedAt(LocalDateTime.now())
                .build();
        historyRepository.save(history);
    }
    
    public Optional<Application> findById(String id) {
        return applicationRepository.findById(id);
    }
    
    public boolean hasAlreadyApplied(String userId, String jobId) {
        return applicationRepository.findByUserIdAndJobId(userId, jobId).isPresent();
    }
    
    public List<Application> findByUserId(String userId) {
        return applicationRepository.findByUserId(userId);
    }
    
    public List<Application> findByCandidateEmail(String candidateEmail) {
        return applicationRepository.findByCandidateEmail(candidateEmail);
    }
    
    public List<Application> findByRecruiterId(String recruiterId) {
        return applicationRepository.findByRecruiterId(recruiterId);
    }
    
    public List<Application> findByJobId(String jobId) {
        return applicationRepository.findByJobId(jobId);
    }
    
    public List<Application> getByJobId(String jobId) {
        return applicationRepository.findByJobId(jobId);
    }
    
    public List<Application> findAll() {
        return applicationRepository.findAll();
    }
    
    public void updateStatus(String applicationId, ApplicationStatus newStatus, String updatedBy) {
        Application app = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Application not found: " + applicationId));
            
        ApplicationStatus currentStatus = app.getStatus();
        
        if (!isValidTransition(currentStatus, newStatus)) {
            throw new IllegalArgumentException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }
            
        System.out.println("Updating application: " + applicationId);
        System.out.println("Before update: " + currentStatus);
            
        app.setStatus(newStatus);
        app.setUpdatedAt(LocalDateTime.now());
        applicationRepository.save(app);
        
        saveHistory(app.getId(), newStatus, updatedBy);
        
        System.out.println("Updated Status: " + newStatus);
        
        // Data Validation Step
        Application updatedApp = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Failed to re-fetch application"));
        System.out.println("Validated Status from DB: " + updatedApp.getStatus());
        
        // Trigger Email Notification for all status updates
        String subject = "Update on your application for " + app.getJobTitle();
        String body = "Dear " + app.getCandidateName() + ",\n\n" +
                      "Your application for the " + app.getJobTitle() + " role at " + app.getCompanyName() + 
                      " has been updated to: " + newStatus.name().replace("_", " ") + ".\n\n" +
                      "Please log in to your dashboard to track your application.\n\n" +
                      "Best regards,\n" + app.getCompanyName();
                      
        emailService.sendRealEmail(app.getCandidateEmail(), subject, body);
    }
    
    private boolean isValidTransition(ApplicationStatus current, ApplicationStatus next) {
        if (current == next) return false;
        
        // REJECTED or SELECTED are terminal states
        if (current == ApplicationStatus.REJECTED || current == ApplicationStatus.SELECTED) {
            return false;
        }
        
        // REJECTED is always allowed from any non-terminal state
        if (next == ApplicationStatus.REJECTED) {
            return true;
        }
        
        // Enforce forward-only progression using Enum ordinals
        // APPLIED(0) -> UNDER_REVIEW(1) -> SHORTLISTED(2) -> INTERVIEW(3) -> SELECTED(4)
        return next.ordinal() > current.ordinal() && next != ApplicationStatus.APPLIED;
    }
    
    public List<ApplicationStatusHistory> getHistoryByApplicationId(String applicationId) {
        return historyRepository.findByApplicationIdOrderByUpdatedAtAsc(applicationId);
    }
    
    public long count() {
        return applicationRepository.count();
    }
}
