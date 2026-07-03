package com.jobportal.controller;

import com.jobportal.dto.UserDto;
import com.jobportal.model.Application;
import com.jobportal.model.ApplicationStatusHistory;
import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.security.CustomUserDetails;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/candidate")
@RequiredArgsConstructor
public class CandidateController {

    private final UserService userService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final FileService fileService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        // Fetch fresh data explicitly to guarantee statuses match BD updates
        model.addAttribute("applications", applicationService.findByUserId(userDetails.getId()));
        return "candidate/dashboard";
    }

    @GetMapping("/jobs")
    public String browseJobs(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("jobs", jobService.searchJobs(keyword));
        return "candidate/jobs";
    }

    @GetMapping("/job/{id}")
    public String jobDetails(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        Job job = jobService.findById(id).orElse(null);
        if (job == null) {
            redirectAttributes.addFlashAttribute("error", "Job not found or has been removed.");
            return "redirect:/candidate/jobs";
        }
        model.addAttribute("job", job);
        return "candidate/job-details";
    }

    @GetMapping("/profile")
    public String profilePage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findById(userDetails.getId()).orElse(null);
        model.addAttribute("user", user);
        return "candidate/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @ModelAttribute UserDto dto,
                                @RequestParam("resume") MultipartFile resumeFile,
                                RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findById(userDetails.getId()).orElseThrow();
            user.setSkills(dto.getSkills());
            user.setExperience(dto.getExperience());
            user.setLocation(dto.getLocation());

            if (!resumeFile.isEmpty()) {
                String fileId = fileService.uploadResume(resumeFile);
                user.setResumeFileId(fileId);
            }

            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error uploading resume.");
        }
        return "redirect:/candidate/profile";
    }

    @PostMapping("/apply/{jobId}")
    public String applyForJob(@PathVariable String jobId, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        if (applicationService.hasAlreadyApplied(userDetails.getId(), jobId)) {
            redirectAttributes.addFlashAttribute("error", "You have already applied for this job.");
            return "redirect:/candidate/jobs";
        }

        User candidate = userService.findById(userDetails.getId()).orElseThrow();
        if (candidate.getResumeFileId() == null) {
            redirectAttributes.addFlashAttribute("error", "Please upload a resume in your profile before applying.");
            return "redirect:/candidate/profile";
        }

        Job job = jobService.findById(jobId).orElseThrow();

        Application application = Application.builder()
                .userId(candidate.getId())
                .recruiterId(job.getRecruiterId())
                .jobId(job.getId())
                .candidateName(candidate.getName())
                .candidateEmail(candidate.getEmail())
                .jobTitle(job.getTitle())
                .companyName(job.getCompanyName())
                .build();

        applicationService.applyForJob(application);
        redirectAttributes.addFlashAttribute("success", "Successfully applied for " + job.getTitle());
        return "redirect:/candidate/dashboard";
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
