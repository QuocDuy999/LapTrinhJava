package com.genderhealthcare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.genderhealthcare.entity.User;
import com.genderhealthcare.service.MenstrualCycleService;

@Controller
public class DashboardController {
    
    @Autowired
    private MenstrualCycleService menstrualCycleService;
    
    @GetMapping("/dashboard")
    public String showDashboard(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        
        // Get current cycle status
        MenstrualCycleService.CycleStatus currentStatus = menstrualCycleService.getCurrentCycleStatus(user);
        
        model.addAttribute("user", user);
        model.addAttribute("currentStatus", currentStatus);
        
        return "dashboard";
    }
    
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }
}