package com.jobportal.controller;

import com.jobportal.service.ApplicationService;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final JobService jobService;
    private final ApplicationService applicationService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", userService.count());
        model.addAttribute("totalJobs", jobService.count());
        model.addAttribute("totalApplications", applicationService.count());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable String id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("success", "User deleted successfully.");
        return "redirect:/admin/users";
    }

    @GetMapping("/jobs")
    public String manageJobs(Model model) {
        model.addAttribute("jobs", jobService.findAll());
        return "admin/jobs";
    }

    @GetMapping("/applications")
    public String manageApplications(Model model) {
        model.addAttribute("applications", applicationService.findAll());
        return "admin/applications";
    }
}
