package com.healthcare.gender.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.gender.model.entity.User;
import com.healthcare.gender.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (user == null || user.getEmail() == null || user.getPassword() == null || user.getName() == null || user.getUsername() == null) {
            return ResponseEntity.badRequest().body("Tên, Email, Username và Mật khẩu không được để trống!");
        }
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email đã tồn tại!");
        }
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Tên đăng nhập đã tồn tại!");
        }
        userService.saveUser(user);
        return ResponseEntity.ok("Đăng ký thành công!");
    }
    

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        boolean isValidUser = userService.validateUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (isValidUser) {
            return ResponseEntity.ok("Đăng nhập thành công!");
        }
        return ResponseEntity.status(401).body("Tên đăng nhập hoặc mật khẩu không đúng!");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
     Optional<User> user = userService.findByEmail(userDetails.getUsername());
     if (user.isPresent()) {
        return ResponseEntity.ok(user.get());
     } else {
        return ResponseEntity.status(404).body("Không tìm thấy người dùng!");
     }
    }
}  
