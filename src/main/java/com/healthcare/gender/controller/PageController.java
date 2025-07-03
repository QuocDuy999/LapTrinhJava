package com.healthcare.gender.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/customer")
    public String customerPage() {
        return "customer"; // Trả về file templates/customer.html
    }
    @GetMapping("/appointment")
    public String appointmentPage() {   
        return "appointment"; // file templates/appointment.html (nếu dùng Thymeleaf)
    }
    @GetMapping("/questions")   
    public String questionsPage() {
        return "questions"; // Trả về file templates/questions.html
    }
    @GetMapping("/admin-role")
    public String adminRolePage() {
        return "admin-role"; // Trả về file templates/admin-role.html
    }
    @GetMapping("/admin-consultation")
    public String adminConsultationPage() {
        return "admin-consultation"; // Trả về file templates/admin-consultation.html
    }
    @GetMapping("/profile")
    public String profilePage() {       
        return "profile"; // Trả về file templates/profile.html
    }
}