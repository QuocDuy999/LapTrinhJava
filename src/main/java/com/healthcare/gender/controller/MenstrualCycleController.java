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
@RequestMapping("/cycle")
public class MenstrualCycleController {

    @Autowired
    private MenstrualCycleRepository cycleRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String showCyclePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        List<MenstrualCycle> cycles = cycleRepository.findByUserId(user.getId());
        model.addAttribute("cycles", cycles);
        return "cycle"; // Trả về file cycle.html
    }

    @PostMapping("/add")
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
        return "redirect:/cycle";
    }
}
