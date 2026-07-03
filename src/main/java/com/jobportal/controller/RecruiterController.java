package com.jobportal.controller;

import com.jobportal.dto.JobDto;
import com.jobportal.model.Application;
import com.jobportal.model.ApplicationStatus;
import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.security.CustomUserDetails;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.FileService;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/recruiter")
@RequiredArgsConstructor
public class RecruiterController {

    private final JobService jobService;
    private final ApplicationService applicationService;
    private final ApplicationRepository applicationRepository;
    private final UserService userService;
    private final FileService fileService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("jobs", jobService.findByRecruiterId(userDetails.getId()));
        return "recruiter/dashboard";
    }

    @GetMapping("/post-job")
    public String postJobForm(Model model) {
        model.addAttribute("jobDto", new JobDto());
        return "recruiter/post-job";
    }

    @PostMapping("/post-job")
    public String saveJob(@ModelAttribute("jobDto") JobDto jobDto, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        User user = userService.findById(userDetails.getId()).orElseThrow();
        
        Job job = Job.builder()
                .title(jobDto.getTitle())
                .description(jobDto.getDescription())
                .skillsRequired(jobDto.getSkillsRequired())
                .experienceRequired(jobDto.getExperienceRequired())
                .salary(jobDto.getSalary())
                .location(jobDto.getLocation())
                .recruiterId(user.getId())
                .companyName(user.getCompanyName())
                .build();
                
        jobService.saveJob(job);
        redirectAttributes.addFlashAttribute("success", "Job posted successfully.");
        return "redirect:/recruiter/dashboard";
    }
    
    @GetMapping("/delete-job/{id}")
    public String deleteJob(@PathVariable String id, RedirectAttributes redirectAttributes) {
        jobService.deleteJob(id);
        redirectAttributes.addFlashAttribute("success", "Job deleted successfully.");
        return "redirect:/recruiter/dashboard";
    }

    @GetMapping("/applications/{jobId}")
    public String viewApplications(@PathVariable String jobId, Model model) {
        System.out.println("Job ID: " + jobId);
        
        Job job = jobService.findById(jobId).orElse(null);
        if (job == null) {
            return "error";
        }
        
        List<Application> applications = applicationService.getByJobId(jobId);
        System.out.println("Applications size: " + applications.size());
        
        model.addAttribute("applications", applications);
        model.addAttribute("job", job);
        
        return "recruiter/applications";
    }

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam String applicationId,
                               @RequestParam String status,
                               RedirectAttributes redirectAttributes) {
        
        System.out.println("Update API HIT");
        System.out.println("Application ID: " + applicationId);
        System.out.println("Status received: " + status);
        
        try {
            Application application = applicationRepository.findById(applicationId).orElseThrow();
            
            System.out.println("Before update: " + application.getStatus());
            
            ApplicationStatus newStatus = ApplicationStatus.valueOf(status);
            application.setStatus(newStatus);
            
            applicationRepository.save(application);   // MUST BE PRESENT
            
            System.out.println("After update: " + application.getStatus());
            
            redirectAttributes.addFlashAttribute("success", "Status updated successfully!");
            return "redirect:/recruiter/applications/" + application.getJobId();
        } catch (Exception e) {
            System.err.println("Error updating status: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error updating status: " + e.getMessage());
            // If we fail before fetching the application, redirect to dashboard as a fallback
            return "redirect:/recruiter/dashboard";
        }
    }
    
    @GetMapping("/candidate-details/{candidateId}")
    public String candidateDetails(@PathVariable String candidateId, Model model) {
        model.addAttribute("candidate", userService.findById(candidateId).orElse(null));
        return "recruiter/candidate-details";
    }

    @GetMapping("/download-resume/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadResume(@PathVariable String fileId) throws IOException {
        ByteArrayResource resource = fileService.downloadResumeResource(fileId);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resume.pdf\"")
                .body(resource);
    }
}
