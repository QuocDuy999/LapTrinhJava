package com.genderhealthcare.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.genderhealthcare.entity.MenstrualCycle;
import com.genderhealthcare.entity.User;
import com.genderhealthcare.service.MenstrualCycleService;

@Controller
@RequestMapping("/cycle")
public class MenstrualCycleController {
    
    @Autowired
    private MenstrualCycleService menstrualCycleService;
    
    @GetMapping("/tracking")
    public String showCycleTracking(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        
        // Get current cycle status
        MenstrualCycleService.CycleStatus currentStatus = menstrualCycleService.getCurrentCycleStatus(user);
        List<MenstrualCycle> recentCycles = menstrualCycleService.getUserMenstrualCycles(user);
        
        model.addAttribute("user", user);
        model.addAttribute("currentStatus", currentStatus);
        model.addAttribute("recentCycles", recentCycles);
        model.addAttribute("today", LocalDate.now());
        
        return "cycle/tracking";
    }
    
    @GetMapping("/add")
    public String showAddCycleForm(Model model) {
        model.addAttribute("menstrualCycle", new MenstrualCycle());
        model.addAttribute("flowIntensities", MenstrualCycle.FlowIntensity.values());
        return "cycle/add";
    }
    
    @PostMapping("/add")
    public String addMenstrualCycle(@RequestParam("periodStartDate") String periodStartDateStr,
                                  @RequestParam("periodEndDate") String periodEndDateStr,
                                  @RequestParam("flowIntensity") MenstrualCycle.FlowIntensity flowIntensity,
                                  @RequestParam(value = "symptoms", required = false) String symptoms,
                                  @RequestParam(value = "mood", required = false) String mood,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        try {
            User user = (User) authentication.getPrincipal();
            LocalDate periodStartDate = LocalDate.parse(periodStartDateStr);
            LocalDate periodEndDate = LocalDate.parse(periodEndDateStr);
            
            menstrualCycleService.addMenstrualCycle(user, periodStartDate, periodEndDate, flowIntensity, symptoms, mood);
            
            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm chu kỳ kinh nguyệt thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi thêm chu kỳ: " + e.getMessage());
        }
        
        return "redirect:/cycle/tracking";
    }
    
    @GetMapping("/calendar/{year}/{month}")
    @ResponseBody
    public List<MenstrualCycle> getCycleCalendar(@PathVariable int year, @PathVariable int month, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return menstrualCycleService.getMenstrualCyclesByMonth(user, year, month);
    }
}