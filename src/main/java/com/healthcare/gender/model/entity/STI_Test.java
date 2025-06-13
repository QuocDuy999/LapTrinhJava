package com.healthcare.gender.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sti_tests")
public class STI_Test {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime appointmentDate;
    
    private String status; // ví dụ: PENDING, COMPLETED
    
    // Liên kết với người dùng đăng ký xét nghiệm
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters và Setters
    public Long getId() {
        return id;
    }    

    public void setId(Long id) {
        this.id = id;
    }    

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }    

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }    

    public String getStatus() {
        return status;
    }    

    public void setStatus(String status) {
        this.status = status;
    }    

    public User getUser() {
        return user;
    }    

    public void setUser(User user) {
        this.user = user;
    }
}
