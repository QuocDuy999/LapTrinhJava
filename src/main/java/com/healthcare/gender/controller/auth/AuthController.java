package com.healthcare.gender.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;

import com.healthcare.gender.model.dto.RegisterDTO;


@Controller
public class AuthController {

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // Trả về file templates/register.html
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute RegisterDTO registerDTO)
     {
      
        // Redirect về trang login sau khi đăng ký thành công
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Trả về file templates/login.html
    }
}