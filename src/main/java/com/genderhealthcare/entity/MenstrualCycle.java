package com.genderhealthcare.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "menstrual_cycles")
public class MenstrualCycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "period_start_date", nullable = false)
    private LocalDate periodStartDate;
    
    @Column(name = "period_end_date")
    private LocalDate periodEndDate;
    
    @Column(name = "cycle_length")
    private Integer cycleLength;
    
    @Column(name = "ovulation_date")
    private LocalDate ovulationDate;
    
    @Column(name = "fertile_window_start")
    private LocalDate fertileWindowStart;
    
    @Column(name = "fertile_window_end")
    private LocalDate fertileWindowEnd;
    
    @Column(name = "next_period_predicted")
    private LocalDate nextPeriodPredicted;
    
    @Enumerated(EnumType.STRING)
    private FlowIntensity flowIntensity;
    
    private String symptoms;
    private String mood;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public enum FlowIntensity {
        LIGHT, NORMAL, HEAVY
    }
    
    // Constructors
    public MenstrualCycle() {}
    
    public MenstrualCycle(User user, LocalDate periodStartDate) {
        this.user = user;
        this.periodStartDate = periodStartDate;
        initializePredictions();
    }
    
    // Private helper method to calculate predictions
    private void initializePredictions() {
        if (user != null && periodStartDate != null) {
            int avgCycleLength = user.getAverageCycleLength();
            
            this.ovulationDate = periodStartDate.plusDays(avgCycleLength - 14);
            this.fertileWindowStart = ovulationDate.minusDays(5);
            this.fertileWindowEnd = ovulationDate.plusDays(1);
            this.nextPeriodPredicted = periodStartDate.plusDays(avgCycleLength);
        }
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public LocalDate getPeriodStartDate() { return periodStartDate; }
    public void setPeriodStartDate(LocalDate periodStartDate) { 
        this.periodStartDate = periodStartDate;
        initializePredictions();
    }
    
    public LocalDate getPeriodEndDate() { return periodEndDate; }
    public void setPeriodEndDate(LocalDate periodEndDate) { this.periodEndDate = periodEndDate; }
    
    public Integer getCycleLength() { return cycleLength; }
    public void setCycleLength(Integer cycleLength) { this.cycleLength = cycleLength; }
    
    public LocalDate getOvulationDate() { return ovulationDate; }
    public void setOvulationDate(LocalDate ovulationDate) { this.ovulationDate = ovulationDate; }
    
    public LocalDate getFertileWindowStart() { return fertileWindowStart; }
    public void setFertileWindowStart(LocalDate fertileWindowStart) { this.fertileWindowStart = fertileWindowStart; }
    
    public LocalDate getFertileWindowEnd() { return fertileWindowEnd; }
    public void setFertileWindowEnd(LocalDate fertileWindowEnd) { this.fertileWindowEnd = fertileWindowEnd; }
    
    public LocalDate getNextPeriodPredicted() { return nextPeriodPredicted; }
    public void setNextPeriodPredicted(LocalDate nextPeriodPredicted) { this.nextPeriodPredicted = nextPeriodPredicted; }
    
    public FlowIntensity getFlowIntensity() { return flowIntensity; }
    public void setFlowIntensity(FlowIntensity flowIntensity) { this.flowIntensity = flowIntensity; }
    
    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    
    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}