package com.example.genderhealthcare.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.genderhealthcare.entity.MenstrualCycle;
import com.example.genderhealthcare.repository.MenstrualCycleRepository;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private MenstrualCycleRepository cycleRepository;

    @GetMapping
    public String customerDashboard(Model model) {
        // Lấy danh sách chu kỳ (không cần đăng nhập)
        List<MenstrualCycle> cycles = cycleRepository.findAll();

        // Lấy chu kỳ gần nhất
        MenstrualCycle latestCycle = cycles.isEmpty() ? null : cycles.get(0);

        // Tính toán ngày rụng trứng và khả năng thụ thai
        LocalDate ovulationDate = latestCycle != null
                ? latestCycle.getLastPeriodDate().plusDays(latestCycle.getCycleLength() - 14)
                : null;

        LocalDate fertileStart = ovulationDate != null ? ovulationDate.minusDays(3) : null;
        LocalDate fertileEnd = ovulationDate != null ? ovulationDate.plusDays(2) : null;

        model.addAttribute("latestCycle", latestCycle);
        model.addAttribute("ovulationDate", ovulationDate);
        model.addAttribute("fertileStart", fertileStart);
        model.addAttribute("fertileEnd", fertileEnd);

        return "customer";  // Trả về file customer.html
    }
}
