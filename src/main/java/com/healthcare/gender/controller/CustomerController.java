package com.healthcare.gender.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.healthcare.gender.model.entity.MenstrualCycle;
import com.healthcare.gender.model.entity.User;
import com.healthcare.gender.repository.MenstrualCycleRepository;
import com.healthcare.gender.repository.UserRepository;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenstrualCycleRepository cycleRepository;

    @GetMapping
    public ResponseEntity<?> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(401).body(Map.of("error", "Chưa đăng nhập"));
        }

        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        // Lấy danh sách role từ đối tượng Authentication
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "name", user.getName(),
                "roles", roles // ✅ Trả thêm danh sách role
        ));

    }

    @PostMapping("/cycle/add")
    public ResponseEntity<?> addCycle(@RequestParam LocalDate lastPeriodDate,
            @RequestParam int cycleLength,
            @RequestParam int periodLength) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Chưa đăng nhập");
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        MenstrualCycle cycle = new MenstrualCycle();
        cycle.setUser(user);
        cycle.setLastPeriodDate(lastPeriodDate);
        cycle.setCycleLength(cycleLength);
        cycle.setPeriodLength(periodLength);

        cycleRepository.save(cycle);

        return ResponseEntity.ok(Map.of("message", "Thêm chu kỳ thành công"));
    }

    @GetMapping("/cycle/latest")
    public ResponseEntity<?> getLatestCycle() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Chưa đăng nhập");
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        MenstrualCycle latestCycle = cycleRepository.findTopByUserOrderByLastPeriodDateDesc(user)
                .orElse(null);

        if (latestCycle == null) {
            return ResponseEntity.ok(Map.of("message", "Chưa có dữ liệu chu kỳ nào"));
        }

        return ResponseEntity.ok(Map.of(
                "lastPeriodDate", latestCycle.getLastPeriodDate(),
                "cycleLength", latestCycle.getCycleLength(),
                "periodLength", latestCycle.getPeriodLength()));
    }
}
