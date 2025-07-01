package com.healthcare.gender.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/customer")
    public String customerPage() {
        return "customer"; // Trả về file templates/customer.html
    }
    @GetMapping("/cycle")
    public String cyclePage() {
        return "cycle"; // file templates/cycle.html (nếu dùng Thymeleaf)
    }
}

