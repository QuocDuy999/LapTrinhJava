package com.example.genderhealthcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.genderhealthcare.entity.MenstrualCycle;

public interface MenstrualCycleRepository extends JpaRepository<MenstrualCycle, Long> {
    List<MenstrualCycle> findByUserId(Long userId);
}
