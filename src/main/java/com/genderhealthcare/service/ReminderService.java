package com.genderhealthcare.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.genderhealthcare.entity.MenstrualCycle;
import com.genderhealthcare.entity.Reminder;
import com.genderhealthcare.entity.User;
import com.genderhealthcare.repository.ReminderRepository;

@Service
@Transactional
public class ReminderService {
    
    @Autowired
    private ReminderRepository reminderRepository;
    
    public void createAutomaticReminders(User user, MenstrualCycle cycle) {
        // Reminder for next period (2 days before)
        if (cycle.getNextPeriodPredicted() != null) {
            Reminder periodReminder = new Reminder(
                user,
                "Kinh nguyệt sắp tới",
                "Chu kỳ kinh nguyệt của bạn dự kiến bắt đầu vào " + cycle.getNextPeriodPredicted(),
                cycle.getNextPeriodPredicted().minusDays(2).atTime(9, 0),
                Reminder.ReminderType.PERIOD_UPCOMING
            );
            reminderRepository.save(periodReminder);
        }
        
        // Reminder for ovulation
        if (cycle.getOvulationDate() != null) {
            Reminder ovulationReminder = new Reminder(
                user,
                "Ngày rụng trứng",
                "Hôm nay là ngày rụng trứng dự kiến của bạn",
                cycle.getOvulationDate().atTime(8, 0),
                Reminder.ReminderType.OVULATION
            );
            reminderRepository.save(ovulationReminder);
        }
        
        // Reminder for fertile window
        if (cycle.getFertileWindowStart() != null) {
            Reminder fertileReminder = new Reminder(
                user,
                "Thời kỳ màu mỡ",
                "Bạn đang bước vào thời kỳ màu mỡ - khả năng thụ thai cao",
                cycle.getFertileWindowStart().atTime(8, 0),
                Reminder.ReminderType.FERTILE_WINDOW
            );
            reminderRepository.save(fertileReminder);
        }
    }
    
    public Reminder createCustomReminder(User user, String title, String description, 
                                       LocalDateTime reminderDate, Reminder.ReminderType type) {
        Reminder reminder = new Reminder(user, title, description, reminderDate, type);
        return reminderRepository.save(reminder);
    }
    
    public List<Reminder> getUserReminders(User user) {
        return reminderRepository.findByUserOrderByReminderDateDesc(user);
    }
    
    public List<Reminder> getDueReminders() {
        return reminderRepository.findDueReminders(LocalDateTime.now());
    }
    
    public void markReminderAsSent(Long reminderId) {
        reminderRepository.findById(reminderId).ifPresent(reminder -> {
            reminder.setSent(true);
            reminderRepository.save(reminder);
        });
    }
    
    public void deleteReminder(Long reminderId) {
        reminderRepository.deleteById(reminderId);
    }
}