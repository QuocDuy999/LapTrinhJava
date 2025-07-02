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

    private String username;

    @Column(length = 2000)
    private String question;

    private String answer;

    private LocalDateTime askedAt;
    private LocalDateTime answeredAt;

    public boolean isAnswered() {
        return answer != null && !answer.trim().isEmpty();
    }
}