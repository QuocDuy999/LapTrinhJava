package com.healthcare.gender.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,columnDefinition = "NVARCHAR(255)")
    private String username;

    @Column(length = 2000)
    private String question;

    private String answer;

    private LocalDateTime askedAt;
    private LocalDateTime answeredAt;
    @Transient
    private String senderName;
    public boolean isAnswered() {
        return answer != null && !answer.trim().isEmpty();
    }
}