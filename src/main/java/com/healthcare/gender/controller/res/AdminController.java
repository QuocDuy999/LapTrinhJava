package com.healthcare.gender.controller.res;

import com.healthcare.gender.model.dto.UserDto;
import com.healthcare.gender.jwt.JwtUtil;
import com.healthcare.gender.model.entity.User;
import com.healthcare.gender.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")  // ✅ Đồng bộ với frontend gọi /api/admin/**
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ Trả danh sách người dùng (email + role)
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userDtos = userRepository.findAll()
            .stream()
            .map(user -> new UserDto(user.getEmail(), user.getRole()))
            .collect(Collectors.toList());

        return ResponseEntity.ok(userDtos);
    }

    // ✅ API kiểm tra thông tin người dùng từ token
    @GetMapping("/profile")
    public ResponseEntity<String> getUserProfile(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thiếu hoặc sai định dạng token!");
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);

        return ResponseEntity.ok("Email: " + email + "\nRole: " + role);
    }

    // ✅ Gán quyền ROLE_x cho người dùng theo email
    @PutMapping("/set-role")
    public ResponseEntity<String> setUserRole(@RequestParam String email, @RequestParam String role) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy người dùng với email: " + email);
        }

        if (role == null || !role.startsWith("ROLE_")) {
            return ResponseEntity.badRequest()
                    .body("Vai trò không hợp lệ. Vai trò phải bắt đầu bằng 'ROLE_' (VD: ROLE_USER, ROLE_CONSULTANT)");
        }

        User user = userOpt.get();
        user.setRole(role);
        userRepository.save(user);

        return ResponseEntity.ok("✅ Đã cập nhật vai trò '" + role + "' cho người dùng: " + email);
    }
}
