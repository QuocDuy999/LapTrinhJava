package com.healthcare.gender.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.healthcare.gender.model.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthcare.gender.model.entity.MenstrualCycle;

public interface MenstrualCycleRepository extends JpaRepository<MenstrualCycle, Long> {
    List<MenstrualCycle> findByUserId(Long userId);
    Optional<MenstrualCycle> findTopByUserOrderByLastPeriodDateDesc(User user);
    MenstrualCycle findTopByUserIdOrderByLastPeriodDateDesc(Long userId);
    Optional<MenstrualCycle> findByUserIdAndLastPeriodDate(Long userId, LocalDate lastPeriodDate);
    MenstrualCycle findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
