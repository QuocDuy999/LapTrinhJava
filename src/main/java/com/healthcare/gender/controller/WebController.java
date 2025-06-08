package com.healthcare.gender.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index"; // trả về index.html
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // trả về login.html
    }
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // Sẽ hiển thị register.html
    }
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";  // tên file template dashboard.html trong thư mục resources/templates
    }
}
