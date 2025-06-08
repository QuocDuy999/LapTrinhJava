package com.genderhealthcare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.genderhealthcare.entity.Reminder;

@Service
public class NotificationService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void sendNotification(Reminder reminder) {
        // Send email notification
        sendEmailNotification(reminder);
        
        // In a real application, you might also send SMS, push notifications, etc.
    }
    
    private void sendEmailNotification(Reminder reminder) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(reminder.getUser().getEmail());
            message.setSubject("Nhắc nhở: " + reminder.getTitle());
            message.setText(reminder.getDescription());
            
            mailSender.send(message);
        } catch (MailException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}