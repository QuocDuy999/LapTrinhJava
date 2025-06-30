package com.healthcare.gender.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import com.healthcare.gender.model.entity.MenstrualCycle;
import com.healthcare.gender.model.entity.User;
import com.healthcare.gender.repository.MenstrualCycleRepository;
import com.healthcare.gender.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
@RestController
@RequestMapping("/api/cycle")
public class MenstrualCycleApiController {

    @Autowired
    private MenstrualCycleRepository cycleRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<MenstrualCycle>> getUserCycles(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        List<MenstrualCycle> cycles = cycleRepository.findByUserId(user.getId());
        return ResponseEntity.ok(cycles);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCycle(Authentication authentication,
                                      @RequestParam LocalDate lastPeriodDate,
                                      @RequestParam int cycleLength,
                                      @RequestParam int periodLength) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (cycleLength < 20 || cycleLength > 40) {
            return ResponseEntity.badRequest().body("Độ dài chu kỳ không hợp lệ");
        }

        MenstrualCycle cycle = new MenstrualCycle();
        cycle.setUser(user);
        cycle.setLastPeriodDate(lastPeriodDate);
        cycle.setCycleLength(cycleLength);
        cycle.setPeriodLength(periodLength);

        cycleRepository.save(cycle);
        return ResponseEntity.ok("Cycle saved");
    }

    @GetMapping("/latest")
    public ResponseEntity<MenstrualCycle> getLatestCycle(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        MenstrualCycle latest = cycleRepository.findTopByUserIdOrderByLastPeriodDateDesc(user.getId());
        if (latest == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(latest);
    }
}
