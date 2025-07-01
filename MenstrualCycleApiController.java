package com.healthcare.gender.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.healthcare.gender.model.entity.MenstrualCycle;
import com.healthcare.gender.model.entity.User;
import com.healthcare.gender.repository.MenstrualCycleRepository;
import com.healthcare.gender.repository.UserRepository;

@RestController
@RequestMapping("/api/cycle")
public class MenstrualCycleApiController {

    @Autowired
    private MenstrualCycleRepository cycleRepository;

    @Autowired
    private UserRepository userRepository;

    // Lấy toàn bộ chu kỳ của người dùng
    @GetMapping
    public ResponseEntity<List<MenstrualCycle>> getUserCycles(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        List<MenstrualCycle> cycles = cycleRepository.findByUserId(user.getId());
        return ResponseEntity.ok(cycles);
    }

    // Thêm hoặc cập nhật chu kỳ (nếu cùng ngày)
    @PostMapping("/add")
    public ResponseEntity<?> addOrUpdateCycle(Authentication authentication,
                                              @RequestParam LocalDate lastPeriodDate,
                                              @RequestParam int cycleLength,
                                              @RequestParam int periodLength) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (cycleLength < 20 || cycleLength > 40) {
            return ResponseEntity.badRequest().body("Độ dài chu kỳ không hợp lệ");
        }

        // Kiểm tra đã tồn tại chu kỳ cùng ngày chưa
        Optional<MenstrualCycle> existing = cycleRepository.findByUserIdAndLastPeriodDate(user.getId(), lastPeriodDate);
        MenstrualCycle cycle;

        if (existing.isPresent()) {
            cycle = existing.get(); // cập nhật bản cũ
        } else {
            cycle = new MenstrualCycle();
            cycle.setUser(user);
            cycle.setLastPeriodDate(lastPeriodDate);
            cycle.setCreatedAt(LocalDateTime.now()); // nhớ set thời gian tạo
        }

        cycle.setCycleLength(cycleLength);
        cycle.setPeriodLength(periodLength);
        cycleRepository.save(cycle);

        return ResponseEntity.ok("Đã lưu hoặc cập nhật chu kỳ");
    }

    // Lấy chu kỳ mới nhất
    @GetMapping("/latest")
    public ResponseEntity<MenstrualCycle> getLatestCycle(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        MenstrualCycle latest = cycleRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId());
        if (latest == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(latest);
    }
}
