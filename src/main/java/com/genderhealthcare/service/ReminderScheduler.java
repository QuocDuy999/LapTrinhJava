package com.genderhealthcare.service;

import com.genderhealthcare.entity.Reminder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReminderScheduler {
    
    @Autowired
    private ReminderService reminderService;
    
    @Autowired
    private NotificationService notificationService;
    
    @Scheduled(fixedRate = 300000) // Check every 5 minutes
    public void checkAndSendReminders() {
        List<Reminder> dueReminders = reminderService.getDueReminders();
        
        for (Reminder reminder : dueReminders) {
            try {
                notificationService.sendNotification(reminder);
                reminderService.markReminderAsSent(reminder.getId());
            } catch (Exception e) {
                System.err.println("Failed to send reminder: " + e.getMessage());
            }
        }
    }
}