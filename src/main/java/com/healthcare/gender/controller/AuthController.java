package com.healthcare.gender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.healthcare.gender.model.dto.RegisterRequest;
import com.healthcare.gender.model.entity.User;
import com.healthcare.gender.repository.UserRepository;



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
   public ResponseEntity<?> processRegister(@RequestBody RegisterRequest request) {
    String name = request.getName();
    String username = request.getUsername();
    String email = request.getEmail();
    String password = request.getPassword();
    String confirmPassword = request.getConfirmPassword();

    if (!password.equals(confirmPassword)) {
        return ResponseEntity.badRequest().body("Mật khẩu xác nhận không khớp!");
    }

    if (userRepository.findByEmail(email).isPresent()) {
        return ResponseEntity.badRequest().body("Email đã được sử dụng!");
    }

    if (userRepository.findByUsername(username).isPresent()) {
        return ResponseEntity.badRequest().body("Tên đăng nhập đã được sử dụng!");
    }

    User user = new User();
    user.setName(name);
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(password));
    user.getRoles().add("ROLE_USER");

    userRepository.save(user);
    return ResponseEntity.ok("Đăng ký thành công!");
}
}
