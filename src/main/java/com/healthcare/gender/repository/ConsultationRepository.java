package com.healthcare.gender.repository;

import com.healthcare.gender.model.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findByUsername(String username);
}