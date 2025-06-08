package com.genderhealthcare.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.genderhealthcare.entity.MenstrualCycle;
import com.genderhealthcare.entity.User;

@Repository
public interface MenstrualCycleRepository extends JpaRepository<MenstrualCycle, Long> {
    List<MenstrualCycle> findByUserOrderByPeriodStartDateDesc(User user);
    
    @Query("SELECT mc FROM MenstrualCycle mc WHERE mc.user = :user AND mc.periodStartDate <= :date ORDER BY mc.periodStartDate DESC")
    List<MenstrualCycle> findByUserAndPeriodStartDateBeforeOrderByPeriodStartDateDesc(@Param("user") User user, @Param("date") LocalDate date);
    
    Optional<MenstrualCycle> findTopByUserOrderByPeriodStartDateDesc(User user);
    
    @Query("SELECT mc FROM MenstrualCycle mc WHERE mc.user = :user AND YEAR(mc.periodStartDate) = :year AND MONTH(mc.periodStartDate) = :month")
    List<MenstrualCycle> findByUserAndYearAndMonth(@Param("user") User user, @Param("year") int year, @Param("month") int month);
}