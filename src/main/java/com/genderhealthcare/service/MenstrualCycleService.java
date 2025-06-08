package com.genderhealthcare.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.genderhealthcare.entity.MenstrualCycle;
import com.genderhealthcare.entity.User;
import com.genderhealthcare.repository.MenstrualCycleRepository;

@Service
@Transactional
public class MenstrualCycleService {
    
    @Autowired
    private MenstrualCycleRepository menstrualCycleRepository;
    
    @Autowired
    private ReminderService reminderService;
    
    public MenstrualCycle addMenstrualCycle(User user, LocalDate periodStartDate, LocalDate periodEndDate, 
                                          MenstrualCycle.FlowIntensity flowIntensity, String symptoms, String mood) {
        MenstrualCycle cycle = new MenstrualCycle(user, periodStartDate);
        cycle.setPeriodEndDate(periodEndDate);
        cycle.setFlowIntensity(flowIntensity);
        cycle.setSymptoms(symptoms);
        cycle.setMood(mood);
        
        // Calculate actual cycle length if there's a previous cycle
        Optional<MenstrualCycle> lastCycle = getLastMenstrualCycle(user);
        if (lastCycle.isPresent()) {
            long cycleLength = ChronoUnit.DAYS.between(lastCycle.get().getPeriodStartDate(), periodStartDate);
            cycle.setCycleLength((int) cycleLength);
        }
        
        MenstrualCycle savedCycle = menstrualCycleRepository.save(cycle);
        
        // Create automatic reminders
        reminderService.createAutomaticReminders(user, savedCycle);
        
        // Update user's average cycle length
        updateUserAverageCycleLength(user);
        
        return savedCycle;
    }
    
    public List<MenstrualCycle> getUserMenstrualCycles(User user) {
        return menstrualCycleRepository.findByUserOrderByPeriodStartDateDesc(user);
    }
    
    public Optional<MenstrualCycle> getLastMenstrualCycle(User user) {
        return menstrualCycleRepository.findTopByUserOrderByPeriodStartDateDesc(user);
    }
    
    public List<MenstrualCycle> getMenstrualCyclesByMonth(User user, int year, int month) {
        return menstrualCycleRepository.findByUserAndYearAndMonth(user, year, month);
    }
    
    public CycleStatus getCurrentCycleStatus(User user) {
        Optional<MenstrualCycle> lastCycle = getLastMenstrualCycle(user);
        if (lastCycle.isEmpty()) {
            return new CycleStatus("Chưa có dữ liệu chu kỳ", 0, LocalDate.now(), false, false, false);
        }
        
        MenstrualCycle cycle = lastCycle.get();
        LocalDate today = LocalDate.now();
        LocalDate periodStart = cycle.getPeriodStartDate();
        int dayOfCycle = (int) ChronoUnit.DAYS.between(periodStart, today) + 1;
        
        boolean isPeriodTime = cycle.getPeriodEndDate() != null && 
                             !today.isBefore(periodStart) && 
                             !today.isAfter(cycle.getPeriodEndDate());
        
        boolean isOvulationTime = cycle.getOvulationDate() != null && 
                                today.isEqual(cycle.getOvulationDate());
        
        boolean isFertileWindow = cycle.getFertileWindowStart() != null && 
                                cycle.getFertileWindowEnd() != null &&
                                !today.isBefore(cycle.getFertileWindowStart()) && 
                                !today.isAfter(cycle.getFertileWindowEnd());
        
        String status = determineStatus(isPeriodTime, isOvulationTime, isFertileWindow, dayOfCycle);
        
        return new CycleStatus(status, dayOfCycle, cycle.getNextPeriodPredicted(), 
                             isPeriodTime, isOvulationTime, isFertileWindow);
    }
    
    private String determineStatus(boolean isPeriodTime, boolean isOvulationTime, boolean isFertileWindow, int dayOfCycle) {
        if (isPeriodTime) {
            return "Đang trong thời kỳ kinh nguyệt";
        } else if (isOvulationTime) {
            return "Ngày rụng trứng";
        } else if (isFertileWindow) {
            return "Thời kỳ màu mỡ - Khả năng thụ thai cao";
        } else {
            return "Ngày " + dayOfCycle + " của chu kỳ";
        }
    }
    
    private void updateUserAverageCycleLength(User user) {
        List<MenstrualCycle> cycles = menstrualCycleRepository.findByUserOrderByPeriodStartDateDesc(user);
        if (cycles.size() >= 2) {
            int totalLength = 0;
            int count = 0;
            
            for (int i = 0; i < cycles.size() - 1; i++) {
                Integer cycleLength = cycles.get(i).getCycleLength();
                if (cycleLength != null) {
                    totalLength += cycleLength;
                    count++;
                }
            }
            
            if (count > 0) {
                user.setAverageCycleLength(totalLength / count);
            }
        }
    }
    
    public static class CycleStatus {
        private final String status;
        private final int dayOfCycle;
        private final LocalDate nextPeriodDate;
        private final boolean isPeriodTime;
        private final boolean isOvulationTime;
        private final boolean isFertileWindow;
        
        public CycleStatus(String status, int dayOfCycle, LocalDate nextPeriodDate, 
                          boolean isPeriodTime, boolean isOvulationTime, boolean isFertileWindow) {
            this.status = status;
            this.dayOfCycle = dayOfCycle;
            this.nextPeriodDate = nextPeriodDate;
            this.isPeriodTime = isPeriodTime;
            this.isOvulationTime = isOvulationTime;
            this.isFertileWindow = isFertileWindow;
        }
        
        // Getters
        public String getStatus() { return status; }
        public int getDayOfCycle() { return dayOfCycle; }
        public LocalDate getNextPeriodDate() { return nextPeriodDate; }
        public boolean isPeriodTime() { return isPeriodTime; }
        public boolean isOvulationTime() { return isOvulationTime; }
        public boolean isFertileWindow() { return isFertileWindow; }
    }
}
