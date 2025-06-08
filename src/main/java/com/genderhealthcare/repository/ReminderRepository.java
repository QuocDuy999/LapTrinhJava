package com.genderhealthcare.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.genderhealthcare.entity.Reminder;
import com.genderhealthcare.entity.User;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByUserOrderByReminderDateDesc(User user);
    
    @Query("SELECT r FROM Reminder r WHERE r.reminderDate <= :now AND r.sent = false")
    List<Reminder> findDueReminders(@Param("now") LocalDateTime now);
    
    @Query("SELECT r FROM Reminder r WHERE r.user = :user AND r.reminderDate >= :start AND r.reminderDate < :end")
    List<Reminder> findByUserAndDateRange(@Param("user") User user, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}