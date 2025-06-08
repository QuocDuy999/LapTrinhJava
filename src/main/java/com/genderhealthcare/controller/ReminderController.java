package com.genderhealthcare.controller;

import java.time.LocalDateTime;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.genderhealthcare.entity.Reminder;
import com.genderhealthcare.entity.User;
import com.genderhealthcare.service.ReminderService;

@Controller
@RequestMapping("/reminders")
public class ReminderController {
    
    @Autowired
    private ReminderService reminderService;
    
    @GetMapping
    public String showReminders(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Reminder> reminders = reminderService.getUserReminders(user);
        
        model.addAttribute("user", user);
        model.addAttribute("reminders", reminders);
        model.addAttribute("reminderTypes", Reminder.ReminderType.values());
        
        return "reminders/list";
    }
    
    @PostMapping("/add")
    public String addReminder(@RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam("reminderDateTime") String reminderDateTimeStr,
                            @RequestParam("type") Reminder.ReminderType type,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {
        try {
            User user = (User) authentication.getPrincipal();
            LocalDateTime reminderDateTime = LocalDateTime.parse(reminderDateTimeStr);
            
            reminderService.createCustomReminder(user, title, description, reminderDateTime, type);
            
            redirectAttributes.addFlashAttribute("successMessage", "Đã tạo nhắc nhở thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }
        
        return "redirect:/reminders";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteReminder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reminderService.deleteReminder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa nhắc nhở!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi xóa nhắc nhở!");
        }
        
        return "redirect:/reminders";
    }
}