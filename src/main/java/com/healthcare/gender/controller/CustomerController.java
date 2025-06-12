package com.healthcare.gender.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.healthcare.gender.model.entity.MenstrualCycle;
import com.healthcare.gender.model.entity.User;
import com.healthcare.gender.repository.MenstrualCycleRepository;
import com.healthcare.gender.repository.UserRepository;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenstrualCycleRepository cycleRepository;

    @GetMapping
    public String customerDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        List<MenstrualCycle> cycles = cycleRepository.findByUserId(user.getId());

        // Lấy chu kỳ gần nhất
        MenstrualCycle latestCycle = cycles.isEmpty() ? null : cycles.get(0);

        // Tính toán ngày rụng trứng và khả năng thụ thai
        LocalDate ovulationDate = latestCycle != null
                ? latestCycle.getLastPeriodDate().plusDays(latestCycle.getCycleLength() - 14)
                : null;

        LocalDate fertileStart = ovulationDate != null ? ovulationDate.minusDays(3) : null;
        LocalDate fertileEnd = ovulationDate != null ? ovulationDate.plusDays(2) : null;

        model.addAttribute("user", user);
        model.addAttribute("latestCycle", latestCycle);
        model.addAttribute("ovulationDate", ovulationDate);
        model.addAttribute("fertileStart", fertileStart);
        model.addAttribute("fertileEnd", fertileEnd);

        return "customer";  // Trả về file customer.html
    }

    @PostMapping("/cycle/add")
    public String addCycle(@AuthenticationPrincipal UserDetails userDetails,
                       @RequestParam LocalDate lastPeriodDate,
                       @RequestParam int cycleLength,
                       @RequestParam int periodLength) {
    String email = userDetails.getUsername();
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
    MenstrualCycle cycle = new MenstrualCycle();
    cycle.setUser(user);
    cycle.setLastPeriodDate(lastPeriodDate);
    cycle.setCycleLength(cycleLength);
    cycle.setPeriodLength(periodLength);

    cycleRepository.save(cycle);
    return "success"; // Trả về phản hồi để AJAX xử lý
  }
}
