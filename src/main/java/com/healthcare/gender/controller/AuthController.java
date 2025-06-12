package com.example.genderhealthcare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.genderhealthcare.entity.User;
import com.example.genderhealthcare.repository.UserRepository;



@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Hiển thị trang đăng nhập
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Trả về file login.html
    }

    // Hiển thị trang đăng ký
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // Trả về file register.html
    }

    // Xử lý đăng ký người dùng
    @PostMapping("/register")
    public String processRegister(@RequestParam String username,
                                  @RequestParam String email,
                                  @RequestParam String password,
                                  @RequestParam String confirmPassword,
                                  Model model) {
        // Kiểm tra mật khẩu xác nhận
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "register";
        }

        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email đã được sử dụng!");
            return "register";
        }

        // Kiểm tra username đã tồn tại chưa
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Tên đăng nhập đã được sử dụng!");
            return "register";
        }

        // Tạo người dùng mới
        User user = new User();
        user.setUsername(username); // ✅ Đã thêm username vào User.java
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Mã hóa mật khẩu
        userRepository.save(user);

        model.addAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "login"; // Chuyển hướng đến trang đăng nhập
    }
}
