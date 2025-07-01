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
}
